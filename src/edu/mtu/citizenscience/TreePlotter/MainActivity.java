package edu.mtu.citizenscience.TreePlotter;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener {

	Spinner spinner;
	private String user;
	private String email;
	private AlertDialog.Builder builder;
	private AlertDialog ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		spinner = (Spinner) findViewById(R.id.Users_spinner);

		spinner.setOnItemSelectedListener( this);
		spinner.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				
				deleteUserDialog().show();
				
				return false;
			}
			
		});

		loadSpinner();
	}
	
	private Dialog deleteUserDialog(){
		
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.delete_row_dialog, null);
		
		builder.setTitle("Delete User Tree");
		builder.setMessage("Press Delete to User");
		builder.setView(layout);
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, user, Toast.LENGTH_SHORT).show();
			}
	
		});
		
		ad = builder.create();
		return ad;
	}
	

	private void loadSpinner() {
		
		ArrayList<String> users = new ArrayList<String>();
		List<User> Current_users = User.listAll(User.class);

		for(User u: Current_users){
			users.add(u.getUsername());
		}

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, users);

		// Drop down layout style - list view with radio button
		dataAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);

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


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.plots_table_button:
			Intent start_pt = new Intent(this, PlotTableActivity.class);
			start_pt.putExtra("user", user);
			start_pt.putExtra("email", email);
			startActivity(start_pt);
			break;
		case R.id.login_button:
			Intent start_l = new Intent(this, LoginActivity.class);
			startActivity(start_l);
			break;
		}
	}//endof onClick

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
	
		// On selecting a spinner item
		String label = arg0.getItemAtPosition(arg2).toString();
		user = label;
		
		List<User> curr_user = User.find(User.class, "username = ?", user);
		email = curr_user.get(0).getEmail();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}


