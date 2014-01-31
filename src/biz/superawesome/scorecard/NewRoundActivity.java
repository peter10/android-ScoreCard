package biz.superawesome.scorecard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import biz.superawesome.scorecard.model.DatabaseHelper;
import biz.superawesome.scorecard.model.Hole;
import biz.superawesome.scorecard.model.Player;
import biz.superawesome.scorecard.model.PlayerRound;
import biz.superawesome.scorecard.model.Round;
import biz.superawesome.scorecard.model.Score;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class NewRoundActivity extends OrmLiteBaseActivity<DatabaseHelper>
		implements OnClickListener {

	RuntimeExceptionDao<Player, Integer> playerDao;
	RuntimeExceptionDao<Round, Integer> roundDao;
	RuntimeExceptionDao<Hole, Integer> holeDao;
	RuntimeExceptionDao<PlayerRound, Integer> playerRoundDao;
	RuntimeExceptionDao<Score, Integer> scoreDao;
	ArrayAdapter<Player> playerAdapter;
	ListView playerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_round);

		playerDao = getHelper().getRuntimeExceptionDao(Player.class);
		playerAdapter = new ArrayAdapter<Player>(this,
				android.R.layout.simple_list_item_multiple_choice,
				playerDao.queryForAll());
		playerView = (ListView) findViewById(R.id.listView1);
		playerView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		playerView.setAdapter(playerAdapter);

		Integer[] rangeOfHoles = { 5, 9, 18, 27 };
		ArrayAdapter<Integer> roundAdapter = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, rangeOfHoles);
		Spinner numOfHoles = (Spinner) findViewById(R.id.spinner1);
		numOfHoles.setAdapter(roundAdapter);
		numOfHoles.setSelection(2);

		Button start = (Button) findViewById(R.id.button1);
		start.setOnClickListener(this);
	}

	// creates new Round and associated records
	private int createRound() {
		roundDao = getHelper().getRuntimeExceptionDao(Round.class);
		holeDao = getHelper().getRuntimeExceptionDao(Hole.class);
		playerRoundDao = getHelper().getRuntimeExceptionDao(PlayerRound.class);
		scoreDao = getHelper().getRuntimeExceptionDao(Score.class);

		// check that they selected some players before we create anything
		SparseBooleanArray playerArray = playerView.getCheckedItemPositions();
		if (playerArray.size() == 0) {
			return -1;
		}

		// create the Round
		Round r = new Round(null);
		roundDao.create(r);

		// save the Players in a pivot table
		for (int i = 0; i < playerArray.size(); i++) {
			if (playerArray.valueAt(i)) {
				int idx = playerArray.keyAt(i);
				Player p = (Player) playerView.getAdapter().getItem(idx);
				PlayerRound pr = new PlayerRound(p, r);
				playerRoundDao.create(pr);
			}
		}

		// create the Holes, add a Score to each hole for every Player
		Spinner numPlayersSpinner = (Spinner) findViewById(R.id.spinner1);
		Integer numPlayers = (Integer) numPlayersSpinner.getSelectedItem();
		for (int i = 0; i < numPlayers; i++) {
			Hole h = new Hole(r, Integer.toString(i + 1), i);
			holeDao.create(h);

			for (int j = 0; j < playerArray.size(); j++) {
				if (playerArray.valueAt(j)) {
					int idx = playerArray.keyAt(j);
					Player p = (Player) playerView.getAdapter().getItem(idx);
					Score s = new Score(h, p);
					scoreDao.create(s);
				}
			}
		}
		return r.id;
	}

	@Override
	public void onClick(View v) {
		int roundId = createRound();
		if (roundId < 0) { // error; they didn't select any players
			new AlertDialog.Builder(this)
		    .setTitle("No players selected")
		    .setMessage("Please select some players")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })
		     .show();
			return;
		}
		Intent i = new Intent(this, RoundActivity.class);
		i.putExtra("ROUND_ID", roundId);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_round, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
        case R.id.main_action:
        	Intent j = new Intent(this, MainActivity.class);
            startActivity(j);
            return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
