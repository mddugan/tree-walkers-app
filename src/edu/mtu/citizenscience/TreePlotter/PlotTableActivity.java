package edu.mtu.citizenscience.TreePlotter;

import java.util.ArrayList;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlotTableActivity extends Activity {

	ArrayList<plots> myPlots = new ArrayList<plots>();
	private AlertDialog.Builder builder;
	private AlertDialog ad;
	private  String plot_name = "plot name" , plot_coor = "coordinates";
	private final int CAMERA_INTENT_CODE = 7;
	private int current_position = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_table);
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


	public void onClick(View v) {

		switch(v.getId()){

		case R.id.new_plot:

			newPlotDialog().show();
			break;


		}
	}

	private Dialog newPlotDialog(){

		//varibles

		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.new_plot_dialog, null);

		final EditText input_plot_name = (EditText)layout.findViewById(R.id.np_plot_name);
		final EditText input_plot_coor = (EditText)layout.findViewById(R.id.np_plot_coor);
		plot_name = "plot name";
		plot_coor = "coordinates";

		builder.setTitle("New Plot");
		builder.setMessage("Enter the plot name and coorinates");
		builder.setView(layout);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				plot_name =  input_plot_name.getText().toString();
				plot_coor = input_plot_coor.getText().toString();

				plotToList(plot_name, plot_coor);
				plotsToDisplay();
			}

		});


		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				plot_name = "plot name";
				plot_coor = "coordinates";
			}
		});
		ad = builder.create();
		return ad;
	}

	private void plotToList(String name, String coordinates) {
		myPlots.add(new plots(name, coordinates, null));
	}

	private void plotsToDisplay(){

		ArrayAdapter <plots> displayPlotsAdapter = new plotDisplayAdapter();
		ListView list = (ListView) findViewById(R.id.plot_element);
		list.setAdapter(displayPlotsAdapter);
	}

	public class plotDisplayAdapter extends ArrayAdapter<plots>{

		public plotDisplayAdapter() {
			super(PlotTableActivity.this, R.layout.plot_table_elements, myPlots);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View plotView = convertView;

			//incase the view is null
			if(plotView == null){
				plotView =  getLayoutInflater().inflate(R.layout.plot_table_elements, parent, false);
			}

			//Find plot
			plots currentPlot = myPlots.get(position);

			//fill the view
			//plot name
			TextView plot_name = (TextView) plotView.findViewById(R.id.plot_name);
			plot_name.setText(currentPlot.getName());

			//plot coordinates
			TextView plot_coor = (TextView) plotView.findViewById(R.id.coordinates);
			plot_coor.setText(currentPlot.getCoordinates());

			//Configuration for the location/gps button on the List view
			final ImageButton location_button = (ImageButton) plotView.findViewById(R.id.pt_location);
			location_button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					switch(v.getId()){
					case R.id.pt_location:
						//Toast.makeText(getApplicationContext(), "location", Toast.LENGTH_SHORT).show();
						current_position = position;
						break;
					}

				}

			});

			//Configuration for the camera button on the List view
			final ImageButton camera_button = (ImageButton) plotView.findViewById(R.id.pt_camera);
			camera_button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					switch(v.getId()){
					case R.id.pt_camera:
					//	Toast.makeText(getApplicationContext(), "camera", Toast.LENGTH_SHORT).show();
						current_position = position;
						startCamera();
						break;
					}
				}


			});

			//Configuration for the TreeInfo button on the List view
			final ImageButton tree_info = (ImageButton)plotView.findViewById(R.id.pt_info);
			tree_info.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					switch(v.getId()){
					case R.id.pt_info:
						//Toast.makeText(getApplicationContext(), "tree_info", Toast.LENGTH_SHORT).show();
						current_position = position;
						startTreeInfo();
						break;
					}

				}

			});

			//Configuration for the Upload button on the List view
			final ImageButton upload_button = (ImageButton)plotView.findViewById(R.id.pt_upload);
			upload_button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					switch(v.getId()){
					case R.id.pt_upload:
						//Toast.makeText(getApplicationContext(), "upload", Toast.LENGTH_SHORT).show();
						current_position = position;
						break;
					}

				}

			});

			return plotView;

		}
	}

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
				Toast.makeText(getApplicationContext(), "current position = "+current_position, Toast.LENGTH_SHORT).show();
				Bitmap img = (Bitmap) data.getExtras().get("data");
				
				myPlots.get(current_position).setImg(img);
				
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed
			}
		}
	}//endof onActivityResult
	
	public void startTreeInfo(){
		Intent TreeInfoIntent = new Intent(this, PlotInfoActivity.class);
		TreeInfoIntent.putExtra("plot_name", myPlots.get(current_position).getName());
		startActivity(TreeInfoIntent);
		
	}
	
}
