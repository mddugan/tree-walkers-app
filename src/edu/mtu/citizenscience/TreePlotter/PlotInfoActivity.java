package edu.mtu.citizenscience.TreePlotter;

import java.util.ArrayList;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
<<<<<<< HEAD
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
=======
>>>>>>> 1b593dd3581232374a17789a437c8616503e1e3e
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PlotInfoActivity extends Activity {

	private ArrayList<Tree> myLargeTrees = new ArrayList<Tree>();
	private ArrayList<Tree> mySmallTrees = new ArrayList<Tree>();
	private AlertDialog.Builder builder;
	private AlertDialog ad;
	private String small_tree_name = " ";
	private String large_tree_name = " ";
	private String abundance_lvl = " ";
	private final int CAMERA_INTENT_CODE = 7;
	private int small_curr_position = -1;
	private int large_curr_position = -1;
	private Bundle extra;
	private String curr_plot_name;
	private int smallTreeRow;
	private int largeTreeRow;
	private boolean isNewSmallTree;
	private boolean isNewLargeTree;
	private String[] abundanceLevels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_info_activity);

		extra = getIntent().getExtras();

		if(extra.getString("plot_name") != null){
			curr_plot_name  = extra.getString("plot_name");
		}

		Button add_small_tree = (Button)findViewById(R.id.pi_add_small);
		add_small_tree.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				smallTreeDialog(null, null).show();
			}

		});

		Button add_large_tree = (Button)findViewById(R.id.pi_add_large);
		add_large_tree.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				largeTreeDialog(null, null).show();

			}


		});

		abundanceLevels = new String[]{"Rare", "Uncommon", "Common"};


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




	private Dialog smallTreeDialog(String aName, String aAbundance) {
		isNewSmallTree = false;

		if(aName == null && aAbundance == null) {
			isNewSmallTree = true;
		}


		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.add_tree_dialog, null);

		final EditText input_tree_name = (EditText)layout.findViewById(R.id.atd_fillname);
		if (!isNewSmallTree) {
			input_tree_name.setText(aName);
		}


		if (isNewSmallTree) {
			builder.setTitle("New Small Tree");
		}
		else {
			builder.setTitle("Edit Small Tree");
		}
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

		if (!isNewSmallTree) {
			int index = 0;
			for(String s : abundanceLevels) {
				if (aAbundance.equals(s)) {
					spinner.setSelection(index);
					break;
				}
				++index;
			}
		}


		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				small_tree_name = input_tree_name.getText().toString();
				if (isNewSmallTree) {
					mySmallTrees.add(new Tree(curr_plot_name ,small_tree_name, abundance_lvl, "Small", null));
				}
				else {
					mySmallTrees.set(smallTreeRow, new Tree(curr_plot_name ,small_tree_name, abundance_lvl, "Small", null));
				}
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

	private Dialog largeTreeDialog(String aName, String aAbundance) {

		isNewLargeTree = false;

		if(aName == null && aAbundance == null) {
			isNewLargeTree = true;
		}

		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.add_tree_dialog, null);

		final EditText input_tree_name = (EditText)layout.findViewById(R.id.atd_fillname);
		if (!isNewLargeTree) {
			input_tree_name.setText(aName);
		}
		
		if (isNewLargeTree) {
			builder.setTitle("New Large Tree");
		}
		else {
			builder.setTitle("Edit Large Tree");
		}
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

		if (!isNewLargeTree) {
			int index = 0;
			for (String s : abundanceLevels) {
				if (aAbundance.equals(s)) {
					spinner.setSelection(index);
				}
				++index;
			}
		}


		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				large_tree_name = input_tree_name.getText().toString();
				if (isNewLargeTree) {
					myLargeTrees.add(new Tree(curr_plot_name, large_tree_name, abundance_lvl, "Large", null));
				}
				else {
					myLargeTrees.set(largeTreeRow, new Tree(curr_plot_name, large_tree_name, abundance_lvl, "Large", null));
				}
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

		ArrayAdapter <Tree> displaySmallTreesAdapter = new SmallTreesDisplayAdapter();
		ListView list = (ListView) findViewById(R.id.pi_small_list);
		list.setAdapter(displaySmallTreesAdapter);
		//add on click listener to detect selection
		list.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//index in view should be same index in plotList
				smallTreeRow = position;
				Tree selectedTree = mySmallTrees.get(smallTreeRow);
				// open edit plot dialog
				smallTreeDialog(selectedTree.getName(), selectedTree.getAbundance()).show();
			}

		});
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				smallTreeRow = position;
				deleteSmallTreeDialog().show();
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
	}

	private void LargeTreesDisplay(){

		ArrayAdapter <Tree> displayLargeTreesAdapter = new LargeTreesDisplayAdapter();
		ListView list = (ListView) findViewById(R.id.pi_large_list);
		list.setAdapter(displayLargeTreesAdapter);
		//add on click listener to detect selection
		list.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//index in view should be same index in plotList
				largeTreeRow = position;
				Tree selectedTree = myLargeTrees.get(largeTreeRow);
				// open edit plot dialog
				largeTreeDialog(selectedTree.getName(), selectedTree.getAbundance()).show();
			}

		});
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				largeTreeRow = position;
				deleteLargeTreeDialog().show();
				// TODO Auto-generated method stub
				return false;
			}
			
		});
	}
	
	
	private Dialog deleteSmallTreeDialog() {
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.delete_plot_dialog, null);
		
		builder.setTitle("Delete Small Tree");
		builder.setMessage("Press Delete to Remove Small Tree");
		builder.setView(layout);
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mySmallTrees.remove(smallTreeRow);
				SmallTreesDisplay();
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
	
	
	private Dialog deleteLargeTreeDialog() {
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.delete_plot_dialog, null);
		
		builder.setTitle("Delete Large Tree");
		builder.setMessage("Press Delete to Remove Large Tree");
		builder.setView(layout);
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				myLargeTrees.remove(largeTreeRow);
				LargeTreesDisplay();
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

	private class SmallTreesDisplayAdapter extends ArrayAdapter<Tree>{


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
			Tree curr_tree = mySmallTrees.get(position);

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
						small_curr_position = position;
						startCamera();
						break;
					}
				}


			});

			return SmallTreeView;

		}

	}


	private class LargeTreesDisplayAdapter extends ArrayAdapter<Tree>{


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
			Tree curr_tree = myLargeTrees.get(position);

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
