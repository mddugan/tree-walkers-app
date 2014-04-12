package edu.mtu.citizenscience.TreePlotter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Help3 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help3, menu);
		return true;
	}

}
