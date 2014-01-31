package biz.superawesome.scorecard;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import biz.superawesome.scorecard.model.DatabaseHelper;
import biz.superawesome.scorecard.model.Hole;
import biz.superawesome.scorecard.model.Round;
import biz.superawesome.scorecard.model.Score;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

/*
 * activity for scoring a hole
 */
public class HoleActivity extends OrmLiteBaseActivity<DatabaseHelper> implements
		OnClickListener, OnItemSelectedListener {

	RuntimeExceptionDao<Hole, Integer> holeDao;
	int holeId;

	ScoreAdapter adapter;
	Hole hole = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hole);

		holeDao = getHelper().getRuntimeExceptionDao(Hole.class);
		holeId = getIntent().getIntExtra("HOLE_ID", -1);
		hole = holeDao.queryForId(holeId);
		if (hole == null) {
			startActivity(new Intent(this, ViewRoundsActivity.class));
			return;
		}

		TextView holeName = (TextView) this.findViewById(R.id.textView1);
		holeName.setText("Hole " + hole.name);

		Score[] scores = hole.scores.toArray(new Score[hole.scores.size()]);
		adapter = new ScoreAdapter(this, scores);

		ListView listView = (ListView) this.findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		// listView.setOnItemSelectedListener(this);

		Button next = (Button) this.findViewById(R.id.button1);
		next.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hole, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// move user to next activity
		Round r = hole.round;
		Intent i;
		if (hole.order < r.holes.size()) {
			i = new Intent(this, HoleActivity.class);
			try {
				QueryBuilder<Hole, Integer> qb = holeDao.queryBuilder(); 
				List<Hole> nextHole = qb.where().eq("order", hole.order + 1).and().eq("round_id", r).query();
				if (nextHole.size() > 0) {
				int nextHoleId = nextHole.get(0).id;
				i.putExtra("HOLE_ID", nextHoleId);
				}
			} catch (SQLException e) {
				// TODO throw a runtime exception
				e.printStackTrace();
			}
		} else {
			i = new Intent(this, RoundActivity.class);
			i.putExtra("ROUND_ID", r.id);
		}
		startActivity(i);
	}

	// save a score when it's been selected
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		int score_value = (Integer) parent.getItemAtPosition(position);
		Score s = (Score) parent.getTag(); // saved this here in ScoreAdapter
		s.score = score_value;
		RuntimeExceptionDao<Score, Integer> dao = getHelper()
				.getRuntimeExceptionDao(Score.class);
		dao.update(s);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// do nothing
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
        case R.id.main_action:
        	Intent j = new Intent(this, MainActivity.class);
            startActivity(j);
            return true;
        case R.id.round_action:
        	Intent k = new Intent(this, RoundActivity.class);
        	k.putExtra("ROUND_ID", hole.round.id);
            startActivity(k);
            return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
