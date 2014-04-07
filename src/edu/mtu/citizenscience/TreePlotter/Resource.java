package edu.mtu.citizenscience.TreePlotter;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
				break;
			case R.id.action_help:
				Toast.makeText(this,"Test context menu selected",Toast.LENGTH_SHORT).show();
		        break;
			default:
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