package biz.superawesome.scorecard;

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
import biz.superawesome.scorecard.model.Round;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class ViewRoundsActivity extends OrmLiteBaseActivity<DatabaseHelper> implements OnItemClickListener {
	RuntimeExceptionDao<Round, Integer> dao;
	ArrayAdapter<Round> adapter;
	Round mRound = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_round);
		
		dao = getHelper().getRuntimeExceptionDao(Round.class);

		adapter = new ArrayAdapter<Round>(this,
			android.R.layout.simple_list_item_1, dao.queryForAll());
		ListView listView = new ListView(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		setContentView(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_rounds, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent i = new Intent(this, RoundActivity.class);
		Round r = (Round) parent.getItemAtPosition(position);
        i.putExtra("ROUND_ID", r.id);
		startActivity(i);
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
