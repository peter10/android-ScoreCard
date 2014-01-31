package biz.superawesome.scorecard;

import java.sql.SQLException;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import biz.superawesome.scorecard.model.DatabaseHelper;
import biz.superawesome.scorecard.model.Hole;
import biz.superawesome.scorecard.model.Player;
import biz.superawesome.scorecard.model.Round;
import biz.superawesome.scorecard.model.Score;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;

public class RoundActivity extends OrmLiteBaseActivity<DatabaseHelper>
	implements OnItemClickListener {

	RuntimeExceptionDao<Round, Integer> roundDao;

	ArrayAdapter<Hole> adapter;
	Round mRound = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		int	roundId = getIntent().getIntExtra("ROUND_ID", -1);
		roundDao = getHelper().getRuntimeExceptionDao(Round.class);
		mRound = roundDao.queryForId(roundId);
		if (mRound == null) {
			startActivity(new Intent(this, ViewRoundsActivity.class));
		}
		
		Hole[] holes = mRound.holes.toArray(new Hole[mRound.holes.size()]);
		adapter = new ArrayAdapter<Hole>(this, android.R.layout.simple_list_item_1, holes);
		ListView listView = new ListView(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		setContentView(listView);
	}
	
	private void showScore() {
		// display a string representation of the current score. A lot of this should be in the model
		int	roundId = mRound.id;
		String currentScore = "";

		PreparedQuery<Player> pq_p;
		try {
			// first get the players we're interested in
			pq_p = getHelper().getRoundPlayersPreparedQuery();
			pq_p.setArgumentHolderValue(0, roundId);
			List<Player> p_list = getHelper().getRuntimeExceptionDao(Player.class).query(pq_p);
			// now we need two more arrays, to hold the score and num of holes played for each player
			int[] scores = new int[p_list.size()];
			int[] numHoles = new int[p_list.size()];
			// iterate through the holes
			for (Hole h : mRound.holes) {
				for (Score s : h.scores) {
					if (s.score > 0) {
						// figure out which player's array index to use
						int i = p_list.indexOf(s.player);
						scores[i] += s.score;
						numHoles[i]++; 
					}
				}
			}
			// format return value
			for (int j=0; j<p_list.size(); j++) {
				currentScore += p_list.get(j).toString() + ": " + scores[j] + " after " + numHoles[j] + "\n";
			}
		} catch (SQLException e) {
			// TODO throw runtime exception
			e.printStackTrace();
		}
		new AlertDialog.Builder(this)
	    .setTitle("Current scores")
	    .setMessage(currentScore)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	     .show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.round, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent i = new Intent(this, HoleActivity.class);
		Hole h = (Hole) parent.getItemAtPosition(position);
        i.putExtra("HOLE_ID", h.id);
		startActivity(i);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
        case R.id.round_score:
        	showScore();
            return true;
        case R.id.round_players_action:
        	Intent i = new Intent(this, RoundPlayersActivity.class);
        	i.putExtra("ROUND_ID", mRound.id);
            startActivity(i);
            return true;
        case R.id.main_action:
        	Intent j = new Intent(this, MainActivity.class);
            startActivity(j);
            return true;
	    default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
