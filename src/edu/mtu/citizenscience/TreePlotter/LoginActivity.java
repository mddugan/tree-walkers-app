package edu.mtu.citizenscience.TreePlotter;

import java.util.List;

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

				final String input_username;
				final String input_emailaddr;
				int validUser = -1;
				int validEmail = -1;
				input_user = (EditText) findViewById(R.id.editUserID);
				input_email = (EditText) findViewById(R.id.editEmail);

				input_username = input_user.getText().toString();
				input_emailaddr = input_email.getText().toString();

				List<User> all_users = User.listAll(User.class);

				if(all_users.size() == 0){

					validUser = checkUsername(input_username, null);
					validEmail = checkEmailAddr(input_emailaddr);

					if((validUser != 0) && (validEmail == 0)){
						Toast.makeText(LoginActivity.this, "Emtpy Username", Toast.LENGTH_SHORT).show();
					}else if((validEmail != 0) && (validUser == 0)){
						Toast.makeText(LoginActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
					}else if((validUser != 0) && (validEmail != 0)){
						Toast.makeText(LoginActivity.this, "Invalid Username & Email", Toast.LENGTH_SHORT).show();
					}else{	

						User user = new User(getBaseContext(), input_username, input_emailaddr, "skill");
						user.save();
					}

				}else{


					for(int i=0; i<all_users.size(); i++){

						validUser = checkUsername(input_username, all_users.get(i).getUsername());
						validEmail = checkEmailAddr(input_emailaddr);

						if((validUser != 0) && (validEmail == 0)){
							Toast.makeText(LoginActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
						}else if((validEmail != 0) && (validUser == 0)){
							Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
						}else if((validUser != 0) && (validEmail != 0)){
							Toast.makeText(LoginActivity.this, "Invalid Username & Email", Toast.LENGTH_SHORT).show();
						}else{
							continue;
						}

					}
				}

				if((validUser == 0) && (validEmail == 0)){
					
					if(all_users.size() != 0){
						User user = new User(getBaseContext(), input_username,input_emailaddr, "skill");
						user.save();
					}


					Intent i= new Intent(LoginActivity.this, MainActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					LoginActivity.this.startActivity(i);
				}

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

	private int checkUsername(String input, String username) {

		if(username == null){
			if(input.isEmpty()){
				return -1;
			}else{
				return 0;
			}
		}else{

			if(input.isEmpty()){
				return -1;
			}else if(username.equalsIgnoreCase(input)){
				return 1;
			}else{
				return 0;

			}
		}

	}//endof checkUsername


	private int checkEmailAddr(String input_emailaddr) {

		if(input_emailaddr.isEmpty()){
			return -1;
		}else if(input_emailaddr.contains("@") == false){
			return 1;
		}else{
			return 0;
		}

	}//endof checkEmailAddr


}


