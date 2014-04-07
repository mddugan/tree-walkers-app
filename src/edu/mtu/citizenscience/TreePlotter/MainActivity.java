package edu.mtu.citizenscience.TreePlotter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.login, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case R.id.help:
				Intent i = new Intent(this, Help.class);
				startActivity(i);
				break;
			case R.id.action_resources:
				Intent d = new Intent(this, Resource.class);
				startActivity(d);
				break;
			case R.id.action_help:
				Toast.makeText(this,"Test context menu selected",Toast.LENGTH_SHORT).show();
		        break;
			default:
				break;
			}
		return false;
	}
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}
	
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.plots_table_button:
			Intent start_pt = new Intent(this, PlotTableActivity.class);
			startActivity(start_pt);
			break;
		case R.id.login_button:
			Intent start_l = new Intent(this, LoginActivity.class);
			startActivity(start_l);
			break;
		}
	}//endof onClick
}


