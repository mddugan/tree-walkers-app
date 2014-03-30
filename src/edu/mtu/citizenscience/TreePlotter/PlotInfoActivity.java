package edu.mtu.citizenscience.TreePlotter;

import java.util.ArrayList;

import edu.mtu.citizenscience.TreePlotter.PlotTableActivity.plotDisplayAdapter;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PlotInfoActivity extends Activity {

	private ArrayList<LargeTree> myLargeTrees = new ArrayList<LargeTree>();
	private ArrayList<SmallTree> mySmallTrees = new ArrayList<SmallTree>();
	private AlertDialog.Builder builder;
	private AlertDialog ad;
	private String small_tree_name = " ";
	private String large_tree_name = " ";
	private String abundance_lvl = " ";
	private final int CAMERA_INTENT_CODE = 7;
	private int small_curr_position = -1;
	private int large_curr_position = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_info_activity);

		Button add_small_tree = (Button)findViewById(R.id.pi_add_small);
		add_small_tree.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				addSmallTreeDialog().show();
			}

		});

		Button add_large_tree = (Button)findViewById(R.id.pi_add_large);
		add_large_tree.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				addLargeTreeDialog().show();

			}


		});




	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.plot_info, menu);
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




	private Dialog addSmallTreeDialog() {

		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.add_tree_dialog, null);

		final EditText input_tree_name = (EditText)layout.findViewById(R.id.atd_fillname);

		builder.setTitle("New Small Tree");
		builder.setMessage("Enter the Small Tree Name and Abundance");

		Spinner spinner = (Spinner)layout.findViewById(R.id.atd_abundance);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.abundance_level, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		builder.setView(layout);


		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){



			public void onNothingSelected(AdapterView<?> parent){

				abundance_lvl = " ";
			}

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,
					long id) {
				abundance_lvl = parent.getSelectedItem().toString();

			}

		});




		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				small_tree_name = input_tree_name.getText().toString();
				mySmallTrees.add(new SmallTree(small_tree_name, abundance_lvl, null));
				SmallTreesDisplay();


			}

		});


		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				small_tree_name = " ";
			}
		});

		ad = builder.create();
		return ad;

	}

	private Dialog addLargeTreeDialog() {

		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.add_tree_dialog, null);

		final EditText input_tree_name = (EditText)layout.findViewById(R.id.atd_fillname);

		builder.setTitle("New Large Tree");
		builder.setMessage("Enter the Large Tree Name and Abundance");

		Spinner spinner = (Spinner)layout.findViewById(R.id.atd_abundance);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.abundance_level, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		builder.setView(layout);


		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){



			public void onNothingSelected(AdapterView<?> parent){

				abundance_lvl = " ";
			}

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,
					long id) {
				abundance_lvl = parent.getSelectedItem().toString();

			}

		});




		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				large_tree_name = input_tree_name.getText().toString();
				myLargeTrees.add(new LargeTree(large_tree_name, abundance_lvl, null));
				LargeTreesDisplay();


			}

		});


		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				large_tree_name = " ";
			}
		});

		ad = builder.create();
		return ad;


	}

	private void SmallTreesDisplay(){

		ArrayAdapter <SmallTree> displaySmallTreesAdapter = new SmallTreesDisplayAdapter();
		ListView list = (ListView) findViewById(R.id.pi_small_list);
		list.setAdapter(displaySmallTreesAdapter);

	}

	private void LargeTreesDisplay(){
		
		ArrayAdapter <LargeTree> displayLargeTreesAdapter = new LargeTreesDisplayAdapter();
		ListView list = (ListView) findViewById(R.id.pi_large_list);
		list.setAdapter(displayLargeTreesAdapter);

	}

	private class SmallTreesDisplayAdapter extends ArrayAdapter<SmallTree>{

		
		public SmallTreesDisplayAdapter() {
			super(PlotInfoActivity.this, R.layout.plot_info_element, mySmallTrees);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			View SmallTreeView = convertView;

			//incase the view is null
			if(SmallTreeView == null){
				SmallTreeView =  getLayoutInflater().inflate(R.layout.plot_info_element, parent, false);
			}
		
			
			//current tree
			SmallTree curr_tree = mySmallTrees.get(position);
			
			//set the name
			TextView name = (TextView) SmallTreeView.findViewById(R.id.pi_tree_name);
			name.setText(curr_tree.getName());
			
			
			
			//set the abundance
			TextView abd = (TextView) SmallTreeView.findViewById(R.id.pi_abundance);
			abd.setText(curr_tree.getAbundance());
			
			//Configuration for the camera button on the List view
			final ImageButton camera_button = (ImageButton) SmallTreeView.findViewById(R.id.pi_camera);
			camera_button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					switch(v.getId()){
					case R.id.pi_camera:
					//	Toast.makeText(getApplicationContext(), "camera", Toast.LENGTH_SHORT).show();
						small_curr_position = position;
						startCamera();
						break;
					}
				}


			});
			
			return SmallTreeView;
			
		}

	}
	
	
private class LargeTreesDisplayAdapter extends ArrayAdapter<LargeTree>{

		
		public LargeTreesDisplayAdapter() {
			super(PlotInfoActivity.this, R.layout.plot_info_element, myLargeTrees);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			View LargeTreeView = convertView;

			//incase the view is null
			if(LargeTreeView == null){
				LargeTreeView =  getLayoutInflater().inflate(R.layout.plot_info_element, parent, false);
			}
		
			
			//current tree
			LargeTree curr_tree = myLargeTrees.get(position);
			
			//set the name
			TextView name = (TextView) LargeTreeView.findViewById(R.id.pi_tree_name);
			name.setText(curr_tree.getName());
			
			
			
			//set the abundance
			TextView abd = (TextView) LargeTreeView.findViewById(R.id.pi_abundance);
			abd.setText(curr_tree.getAbundance());
			
			//Configuration for the camera button on the List view
			final ImageButton camera_button = (ImageButton) LargeTreeView.findViewById(R.id.pi_camera);
			camera_button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					switch(v.getId()){
					case R.id.pi_camera:
					//	Toast.makeText(getApplicationContext(), "camera", Toast.LENGTH_SHORT).show();
						large_curr_position = position;
						startCamera();
						break;
					}
				}


			});
				
			return LargeTreeView;
			
		}
	}
	
	//For the Camera
	public void startCamera() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_INTENT_CODE);
	}//endof startCamera

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAMERA_INTENT_CODE){
			if (resultCode == RESULT_OK) {
				// Image captured
				Bitmap img = (Bitmap) data.getExtras().get("data");
				
				if(small_curr_position > 0){
					mySmallTrees.get(small_curr_position).setImg(img);
					small_curr_position = -1;
				}
				if(large_curr_position > 0){
					myLargeTrees.get(large_curr_position).setImg(img);
					large_curr_position = -1;
				}
				
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed
			}
		}
	}//endof onActivityResult

}
