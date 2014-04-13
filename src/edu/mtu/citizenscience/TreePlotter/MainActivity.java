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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener {

	Spinner spinner;
	private String user;
	private String email;
	private AlertDialog.Builder builder;
	private AlertDialog ad;
	private AlertDialog dd;
	private String new_username;
	private String new_email;
	private int validUser = -1;
	private int validEmail = -1;
	private int invalid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		spinner = (Spinner) findViewById(R.id.Users_spinner);

		spinner.setOnItemSelectedListener( this);

		spinner.setLongClickable(true);

		spinner.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {

				editUserDialog().show();
				return true;
			}

		});

		loadSpinner();
	}

	private Dialog editUserDialog(){

		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.userinfo_edit_dialog, null);

		builder.setTitle("Edit User");
		builder.setMessage("Press Ok&Save to save, Delete to delete user");
		builder.setView(layout);
		final EditText edit_username = (EditText) layout.findViewById(R.id.userinfo_editUserID);
		final EditText edit_email = (EditText) layout.findViewById(R.id.userinfo_editEmail);
		
		if(invalid == 0){
			edit_username.setText(user);
			edit_email.setText(email);
		}else{
			edit_username.setText(new_username);
			edit_email.setText(new_email);
		}

		Button delete = (Button)layout.findViewById(R.id.userinfo_delete);
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				deleteUserDialog().show();
			}

		});

		builder.setPositiveButton("Ok & Save", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				

				new_username = edit_username.getText().toString();
				new_email = edit_email.getText().toString();

				List<User> all_user = User.listAll(User.class);

				for(int i=0; i< all_user.size(); i++){
					validUser = checkUsername(new_username, all_user.get(i).getUsername());
					validEmail = checkEmailAddr(new_email);
					
					if((validUser != 0) && (validEmail == 0)){
						Toast.makeText(MainActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
						invalid = -1;
						ad.dismiss();
						editUserDialog().show();
					}else if((validEmail != 0) && (validUser == 0)){
						Toast.makeText(MainActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
						invalid = -1;
						ad.dismiss();
						editUserDialog().show();
					}else if((validUser != 0) && (validEmail != 0)){
						Toast.makeText(MainActivity.this, "Invalid Username & Email", Toast.LENGTH_SHORT).show();
						invalid = -1;
						ad.dismiss();
						editUserDialog().show();
					}else{	

						continue;
					}
				}
				
				if((validUser == 0) && (validEmail == 0)){
					invalid = 0;
					editUserinfo(user, new_username, new_email);
				}
				

			}


		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub


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
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		// On selecting a spinner item
		String label = parent.getItemAtPosition(position).toString();
		user = label;

		List<User> curr_user = User.find(User.class, "username = ?", user);
		email = curr_user.get(0).getEmail();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}


	private void deleteAll(String user) {

		String plot_name;
		List<User> user_to_delete = User.find(User.class, "username = ?", user);
		List<Plot> user_plot_to_delete = Plot.find(Plot.class, "user = ?", user);

		for(int i=0; i<user_to_delete.size(); i++){
			user_to_delete.get(i).delete();
		}

		for(int i=0; i<user_plot_to_delete.size(); i++){
			plot_name = user_plot_to_delete.get(i).getName();
			List<Tree> plot_trees_to_deleteList = Tree.find(Tree.class, "plot = ?", plot_name);

			for(int j=0; j<plot_trees_to_deleteList.size(); j++){
				plot_trees_to_deleteList.get(j).delete();
			}

			user_plot_to_delete.get(i).delete();
		}

		loadSpinner();
	}//endof deleteAll

	private void editUserinfo(String user, String new_username, String new_email) {

		String plot_name;

		List<User> edit_user = User.find(User.class, "username = ?", user);
		List<Plot> edit_user_plot = Plot.find(Plot.class, "user = ?", user);

		for(int i=0; i<edit_user.size(); i++){
			edit_user.get(i).setUsername(new_username);
			edit_user.get(i).setEmail(new_email);
			edit_user.get(i).save();
		}

		for(int i=0; i<edit_user_plot.size(); i++){
			plot_name = edit_user_plot.get(i).getName();
			List<Tree> edit_trees = Tree.find(Tree.class, "plot = ?", plot_name);

			for(int j=0; j<edit_trees.size(); j++){
				edit_trees.get(j).setPlot_name(plot_name);
				edit_trees.get(j).save();
			}

			edit_user_plot.get(i).setUser(new_username);
			edit_user_plot.get(i).save();

		}

		loadSpinner();
	}//endof editUserinfo

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
	

	private Dialog deleteUserDialog() {
		
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.delete_user_dialog, null);

		builder.setTitle("Delete User");
		builder.setMessage("Deleting User and all plots under User");
		builder.setView(layout);
		
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				deleteAll(user);
				ad.dismiss();
			}
		
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		dd = builder.create();
		return dd;
	}//endof deleteUserDialog


}


