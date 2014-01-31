package biz.superawesome.scorecard;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import biz.superawesome.scorecard.model.DatabaseHelper;
import biz.superawesome.scorecard.model.Player;
import biz.superawesome.scorecard.model.PlayerRound;
import biz.superawesome.scorecard.model.Round;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

public class RoundPlayersActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	
	Round mRound = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// setContentView(R.layout.activity_player);
		
		super.onCreate(savedInstanceState);
		
		int	roundId = getIntent().getIntExtra("ROUND_ID", -1);

		PreparedQuery<Player> pq_p;
		try {
			pq_p = getHelper().getRoundPlayersPreparedQuery();
			pq_p.setArgumentHolderValue(0, roundId);
			List<Player> p_list = getHelper().getRuntimeExceptionDao(Player.class).query(pq_p);
			ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, p_list );
			ListView listView = new ListView(this);
			listView.setAdapter(adapter);
			setContentView(listView);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.round_players, menu);
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
