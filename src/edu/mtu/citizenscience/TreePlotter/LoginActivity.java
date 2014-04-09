package edu.mtu.citizenscience.TreePlotter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private EditText input_user;
	private EditText input_email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	
	
		    	input_user = (EditText) findViewById(R.id.editUserID);
		    	input_email = (EditText) findViewById(R.id.editEmail);
		    	
		    	User user = new User(getBaseContext(), input_user.getText().toString(), input_email.getText().toString(), "skill", null);
		    	user.save();
		    		
		        Intent i= new Intent(LoginActivity.this, MainActivity.class);
		        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        LoginActivity.this.startActivity(i);
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
			case R.id.action_resources:
				Intent d = new Intent(this, Resource.class);
				startActivity(d);
				break;
			case R.id.action_help:
				Intent i = new Intent(this, Help.class);
				startActivity(i);
				break;
			default:
				break;
			}
		return false;
	}
	
	
}


