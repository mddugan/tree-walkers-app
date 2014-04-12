package edu.mtu.citizenscience.TreePlotter;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PlotInfoActivity extends Activity {

	private List<Tree> myLargeTrees;
	private List<Tree> mySmallTrees;
	private AlertDialog.Builder builder;
	private AlertDialog ad;
	private AlertDialog dd;
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
	private boolean isEditingSmallTree;
	private int invaildSmall = 0;
	private int invalidLarge = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_info_activity);

		extra = getIntent().getExtras();

		if(extra.getString("plot_name") != null){
			curr_plot_name  = extra.getString("plot_name");

			//get all the trees for the current plot
			myLargeTrees = Tree.find(Tree.class, "plot = ? and size = ?", curr_plot_name, "Large");
			mySmallTrees = Tree.find(Tree.class, "plot = ? and size = ?", curr_plot_name, "Small");

			//display all the trees a plot has
			SmallTreesDisplay();
			LargeTreesDisplay();
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
			Intent i = new Intent(this, Help3.class);
			startActivity(i);
			break;
		default:
			break;
		}
		return false;
	}

	private Dialog smallTreeDialog(String aName, String aAbundance) {
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.add_tree_dialog, null);
		final EditText input_tree_name = (EditText)layout.findViewById(R.id.atd_fillname);
		isNewSmallTree = false;
		isEditingSmallTree = true;
		
		if((aName == null && aAbundance == null) || (invaildSmall == 1)) {
			Button deleteTree = (Button) layout.findViewById(R.id.delete_tree);
			deleteTree.setVisibility(View.GONE);
			isNewSmallTree = true;
		}


		
		if (!isNewSmallTree) {
			input_tree_name.setText(aName);
		}


		if (isNewSmallTree || (invaildSmall == 1)) {
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
				int index;

				small_tree_name = input_tree_name.getText().toString();

				int valid = checkTreeName(small_tree_name);

				if(valid != 0){
					invaildSmall = 1;
					smallTreeDialog(small_tree_name, abundance_lvl).show();
				}
				else{

					invaildSmall = 0;

					if (isNewSmallTree) {
						mySmallTrees.add(new Tree(getBaseContext(), curr_plot_name ,small_tree_name, abundance_lvl, "Small", null));
						index = mySmallTrees.size()-1;
						mySmallTrees.get(index).save();
					}
					else {
						mySmallTrees.get(smallTreeRow).delete();
						mySmallTrees.set(smallTreeRow, new Tree(getBaseContext(), curr_plot_name ,small_tree_name, abundance_lvl, "Small", null));
						mySmallTrees.get(smallTreeRow).save();
					}
					SmallTreesDisplay();


				}
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

	private int checkTreeName(String tree_name) {

		if(tree_name.isEmpty()){
			return -1;
		}else{
			return 0;
		}

	}




	private Dialog largeTreeDialog(String aName, String aAbundance) {
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.add_tree_dialog, null);

		final EditText input_tree_name = (EditText)layout.findViewById(R.id.atd_fillname);
		isNewLargeTree = false;
		isEditingSmallTree = false;

		if((aName == null && aAbundance == null) || (invalidLarge == 1)) {
			isNewLargeTree = true;
			Button deleteTree = (Button) layout.findViewById(R.id.delete_tree);
			deleteTree.setVisibility(View.GONE);
		}

		
		if (!isNewLargeTree) {
			input_tree_name.setText(aName);
		}

		if (isNewLargeTree || (invalidLarge == 1)) {
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
				int index;
				large_tree_name = input_tree_name.getText().toString();

				int valid = checkTreeName(large_tree_name);

				if(valid != 0){
					invalidLarge = 1;
					largeTreeDialog(large_tree_name, abundance_lvl).show();
					
				}else{
					invalidLarge = 0;
					if (isNewLargeTree) {
						myLargeTrees.add(new Tree(getBaseContext() ,curr_plot_name, large_tree_name, abundance_lvl, "Large", null));
						index = myLargeTrees.size()-1;
						myLargeTrees.get(index).save();
					}
					else {
						myLargeTrees.get(largeTreeRow).delete();
						myLargeTrees.set(largeTreeRow, new Tree(getBaseContext(), curr_plot_name, large_tree_name, abundance_lvl, "Large", null));
						myLargeTrees.get(largeTreeRow).save();
					}

					LargeTreesDisplay();
				}


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

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				smallTreeRow = position;
				Tree selectedTree = mySmallTrees.get(smallTreeRow);
				// open edit plot dialog
				smallTreeDialog(selectedTree.getName(), selectedTree.getAbundance()).show();
				//deleteSmallTreeDialog().show();
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
				//deleteLargeTreeDialog().show();
				Tree selectedTree = myLargeTrees.get(largeTreeRow);
				// open edit plot dialog
				largeTreeDialog(selectedTree.getName(), selectedTree.getAbundance()).show();
				return false;
			}

		});
	}

	public void verifyDelete(View v) {
		if (isEditingSmallTree) {
			deleteSmallTreeDialog().show();
		}
		else {
			deleteLargeTreeDialog().show();
		}
		
	}
	
	private Dialog deleteSmallTreeDialog() {
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.delete_row_dialog, null);
		
		builder.setTitle("Remove Small Tree");
		builder.setMessage("Remove Small Tree?");
		builder.setView(layout);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				mySmallTrees.get(smallTreeRow).delete();
				mySmallTrees.remove(smallTreeRow);
				SmallTreesDisplay();
				ad.dismiss();
			}

		});


		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub


			}
		});
		dd = builder.create();
		return dd;
	}

	private Dialog deleteLargeTreeDialog() {
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.delete_row_dialog, null);
		
		builder.setTitle("Remove Large Tree");
		builder.setMessage("Delete Large Tree?");
		builder.setView(layout);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				myLargeTrees.get(largeTreeRow).delete();
				myLargeTrees.remove(largeTreeRow);
				LargeTreesDisplay();
				ad.dismiss();
			}

		});


		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub


			}
		});
		dd = builder.create();
		return dd;
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
					mySmallTrees.get(small_curr_position).save();
					small_curr_position = -1;
				}
				if(large_curr_position > 0){
					myLargeTrees.get(large_curr_position).setImg(img);
					myLargeTrees.get(large_curr_position).save();
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
