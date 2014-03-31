package edu.mtu.citizenscience.TreePlotter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        Intent i= new Intent(LoginActivity.this, MainActivity.class);
		        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        LoginActivity.this.startActivity(i);
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
				break;
			}
		return false;
	}
	
	
}


