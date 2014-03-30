package edu.mtu.citizenscience.TreePlotter;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Resource extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resource);
		
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.treesofnorthamerica.net/"));
            	startActivity(browserIntent);
            }
        });
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
				//Do nothing
				break;
			default:
				view.setText("Debug.");
				break;
			}
		return false;
		}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_resource,
					container, false);
			return rootView;
		}
	}

}