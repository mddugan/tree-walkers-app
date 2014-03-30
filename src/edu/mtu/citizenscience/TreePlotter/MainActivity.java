package edu.mtu.citizenscience.TreePlotter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,0,0,"Help");
		menu.add(0,1,0,"Resources");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		TextView view = (TextView) findViewById(R.id.text_view);
		switch (item.getItemId()) {
			case 0:
				Intent i = new Intent(this, Help.class);
				startActivity(i);
				break;
			case 1:
				Intent d = new Intent(this, Resource.class);
				startActivity(d);
				break;
			default:
				view.setText("Debug.");
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
		}


	}//endof onClick
}


