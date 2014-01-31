package biz.superawesome.scorecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.player_action:
	            startActivity(new Intent(this, PlayerActivity.class));
	            return true;
	        case R.id.new_round_action:
	            startActivity(new Intent(this, NewRoundActivity.class));
	            return true;
	        case R.id.view_rounds_action:
	            startActivity(new Intent(this, ViewRoundsActivity.class));
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
