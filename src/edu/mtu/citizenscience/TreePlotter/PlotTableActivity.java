package edu.mtu.citizenscience.TreePlotter;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class PlotTableActivity extends Activity {

	private ArrayList<Plot> myPlots = new ArrayList<Plot>();
	private AlertDialog.Builder builder;
	private AlertDialog ad;
	private AlertDialog dd;
	private AlertDialog gd;
	private  String plot_name = "plot name" , plot_lat = "latitude", plot_long = "longitude";
	private final int CAMERA_INTENT_CODE = 7;
	private int current_position = -1;
	private EditText input_plot_lat;
	private EditText input_plot_long;
	private boolean isNewPlot;
	private int plotRow;
	private LocationManager lManager;
	private LocationListener gListener;
	private Location bestLocation;
	private AsyncTask<Void,Void,Boolean> currentGPSTasker;
	private ProgressBar gpsProgress;

	private Bundle extra;
	private String curr_user;
	private List<User> user;

	@SuppressWarnings("static-access")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_table);
		//android.app.ActionBar actionBar = getActionBar();
		//actionBar.setTitle("Plot Table");
		//actionBar.show();

		//get User plots
		extra = getIntent().getExtras();

		if(extra.getString("user") != null){
			curr_user = extra.getString("user");
			user = User.find(User.class, "username = ?", curr_user);

			if(user.get(0).getUser_plots() == null){

				makeToast();

			}

		}
		//*/
		/**
		if(extra.getString("user") != null){
			curr_user = extra.getString("user");

			List<Plot> user_plots = Plot.listAll(Plot.class);

			if(user_plots.size() > 0){
				for(Plot p: user_plots){
					if(p.getUser().equals(curr_user)){
						myPlots.add(p);
					}
				}
			}else{
				myPlots = new ArrayList<Plot>();
			}

		}
		//*/

		//set up LocationManager and Listener for GPS
		currentGPSTasker = null;
		//sets up a location Manager
		lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if (lManager != null) {
			bestLocation = lManager.getLastKnownLocation(lManager.GPS_PROVIDER);
		}
		else {
			Log.e("GPS", "unable to get location manager");
		}

		//set up Location listener
		gListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				Log.d("GPS Listener", "location has changed");
				if (bestLocation == null) {
					Log.d("GPS Listener", "setting location for the first time");
					bestLocation = location;
				}
				else { //compare them
					//check that it is newer than bestLocation
					if (location.getTime() - bestLocation.getTime() > 0 ) {
						Log.d("GPS Listener", "new location is newer than previous location");
						//check that it is more accurate
						if (location.getAccuracy() < bestLocation.getAccuracy()) {
							Log.d("GPS Listener", "new location is more accurate, setting");
							bestLocation = location;

						}
					}

				}
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.d("GPS", provider + "has been enabled");
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.d("GPS", provider + "has been disabled");
			}

		};
	}

	@SuppressLint("ShowToast")
	private void makeToast() {
		Toast.makeText(getBaseContext(), "You got here", Toast.LENGTH_SHORT);
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


	public void onClick(View v) {

		switch(v.getId()){

		case R.id.new_plot:

			plotDialog(null, null, null).show();


			break;


		}
	}


	private Dialog plotDialog(String aName, String aLatitude, String aLongitude){
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.new_plot_dialog, null);

		final EditText input_plot_name = (EditText)layout.findViewById(R.id.np_plot_name);
		input_plot_lat = (EditText)layout.findViewById(R.id.np_plot_lat);
		input_plot_long = (EditText) layout.findViewById(R.id.np_plot_long);
		gpsProgress = (ProgressBar) layout.findViewById(R.id.gps_progress);
		
		//varibles
		isNewPlot = false;
		if (aName == null && aLatitude == null && aLongitude == null) {
			isNewPlot = true;
			Button deletePlot = (Button) layout.findViewById(R.id.delete_plot);
			deletePlot.setVisibility(View.GONE);
		}
		

		//fill in if editing plot
		if(!isNewPlot) {
			input_plot_name.setText(aName);
			input_plot_lat.setText(aLatitude);
			input_plot_long.setText(aLongitude);
		}


		plot_name = "plot name";
		plot_lat = "latitude";
		plot_long = "longitude";

		if (isNewPlot) {
			builder.setTitle("New Plot");
		}
		else {
			builder.setTitle("Edit Plot");
		}
		builder.setMessage("Enter the plot name and coorinates");
		builder.setView(layout);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				plot_name =  input_plot_name.getText().toString();
				plot_lat = input_plot_lat.getText().toString();
				plot_long = input_plot_long.getText().toString();
				try {
					double lon = Double.parseDouble(plot_long);
					double lat = Double.parseDouble(plot_lat);
					if (lat > 90) {
						plot_lat = Double.toString(90.0);
					}
					else if (lat < -90) {
						plot_lat = Double.toString(-90.0);
					}
					else {
						plot_lat = Double.toString(lat);
					}
					
					if (lon > 180) {
						plot_long = Double.toString(180.0);
					}
					
					else if (lon < -180) {
						plot_long = Double.toString(-180.0);
					}
					else {
						plot_long = Double.toString(lon);
					}
					
				}
				catch (Exception e){
					Log.e("GPS", "failed to parse longitude as double");
					//TODO notify user gps coords are invalid
					plot_lat = "";
					plot_long = "";
				}
				
				plotToList(plot_name, plot_lat, plot_long);
				plotsToDisplay();
			}

		});


		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				plot_name = "plot name";
				plot_lat = "latitude";
				plot_long = "longitude";

			}
		});
		ad = builder.create();
		return ad;
	}

	private class GPSUpdaterTask extends AsyncTask<Void, Void, Boolean> {
		private final float MAX_INACCURACY = 250; //in meters
		private final long MAX_ELAPSED_TIME = 1000 * 60; //in milliseconds
		String provider;
		Location updated;
		long startTime;
		@Override
		protected void onPreExecute() {
			provider = lManager.GPS_PROVIDER;
			//start grabbing gps coordinates
			lManager.requestLocationUpdates(provider, 0, 0, gListener);

			Log.d("GPS", "started listening for updates");

			//grab latest location
			updated = lManager.getLastKnownLocation(provider);
			if (updated == null) {
				Log.d("GPS", "updated is null");
			}
			if (bestLocation == null) {
				Log.d("GPS", "bestLocation is null");
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean isGPSValid = false;
			startTime = System.currentTimeMillis();
			long currentTime = -1;
			//wait for more recent location update
			if(updated == null) {
				Log.e("GPS", "updated is null");
			}
			if(bestLocation == null) {
				Log.e("GPS", "bestLocation is null");
			}
			while(updated.getTime() <= bestLocation.getTime()) { //TODO <--- check if null
				currentTime = System.currentTimeMillis();
				if (currentTime - startTime > MAX_ELAPSED_TIME) {
					Log.d("GPS", "GPS timeout");
					return isGPSValid;
				}
				updated = lManager.getLastKnownLocation(provider);
			}

			//now wait for an accurate one
			while(updated.getAccuracy() > MAX_INACCURACY) {
				currentTime = System.currentTimeMillis();
				if (currentTime - startTime > MAX_ELAPSED_TIME) {
					Log.d("GPS", "GPS timeout");
					return isGPSValid;
				}
				updated = lManager.getLastKnownLocation(provider);
			}

			Log.d("GPS", "location is accurate enough");
			isGPSValid = true;
			//set new bestLocation
			bestLocation = updated;
			
			//stop listening
			lManager.removeUpdates(gListener);

			Log.d("GPS", "turned off location updates");

			return isGPSValid;
		}

		protected void onPostExecute(Boolean isGPSValid) {
			gpsProgress.setVisibility(View.GONE);
			lManager.removeUpdates(gListener);
			if (!isGPSValid) {
				unableToRetrieveGPSDialog().show();
				return;
			}
			
			if (bestLocation == null) {
				Log.d("GPS", "bestLocation is null");
				return;
			}


			if (input_plot_lat == null) {
				Log.d("GPS", "latitude field is null");
				return;
			}
			input_plot_lat.setText(Double.toString(bestLocation.getLatitude()));

			if (input_plot_long == null) {
				Log.d("GPS", "longitude field is null");
				return;
			}
			
			//TODO if v is null, failed to get current gps coordinates,
			//should not auto fill and should notify user that unable to get accurate gps coordinates
			input_plot_long.setText(Double.toString(bestLocation.getLongitude()));

		}

	}
	
	
	private Dialog unableToRetrieveGPSDialog() {
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.delete_row_dialog, null);
		
		builder.setTitle("Failed to get GPS");
		builder.setMessage("Unable to get GPS Coordinates");
		builder.setView(layout);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
			}

		});


		
		gd = builder.create();
		return gd;
	}

	public void getGPS(View v) {

		//TODO  device has google services?

		Log.d("GPS", "creating thread to get gps coordinates");
		if (lManager.isProviderEnabled(lManager.GPS_PROVIDER)) {
			Log.d("GPS", "GPS Provider enabled");
		}
		else {
			//show alert telling user that GPS is disabled
			Log.d("GPS", "GPS Provider disabled");
			return;
		}

		if (lManager.isProviderEnabled(lManager.NETWORK_PROVIDER)) {
			Log.d("GPS", "Network Provider enabled");
		}
		else {
			Log.d("GPS", "network Provider disabled");
		}

		if (currentGPSTasker == null) {
			currentGPSTasker = new GPSUpdaterTask().execute(new Void[1]);
			gpsProgress.setVisibility(View.VISIBLE);
		}
		else {
			if (currentGPSTasker.getStatus() == AsyncTask.Status.FINISHED){
				currentGPSTasker = new GPSUpdaterTask().execute(new Void[1]);
				gpsProgress.setVisibility(View.VISIBLE);
			}
			else {
				Log.d("GPS", "GPS Thread already running");
			}
		}

	}

	private void plotToList(String name, String latitude, String longitude) {
		int index;
		if (isNewPlot) {
			myPlots.add(new Plot(getBaseContext(), curr_user,name, latitude, longitude, null));
			index = myPlots.size()-1;
			myPlots.get(index);
		}
		else {//update plot info in arraylist
			myPlots.set(plotRow, new Plot(getBaseContext(), curr_user,name, latitude, longitude, null));
		}
	}

	private void plotsToDisplay(){

		ArrayAdapter <Plot> displayPlotsAdapter = new plotDisplayAdapter();
		ListView list = (ListView) findViewById(R.id.plot_element);
		list.setAdapter(displayPlotsAdapter);
		//add on click listener to detect selection
		
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				plotRow = position;
				Plot selectedPlot = myPlots.get(plotRow);
				// open edit plot dialog
				plotDialog(selectedPlot.getName(), selectedPlot.getLatitude(), selectedPlot.getLongitude()).show();
				//deleteDialog().show();
				// TODO Auto-generated method stub
				return false;
			}
			
		});
	}
	
	public void verifyDelete(View v) {
		deleteDialog().show();
	}
	
	private Dialog deleteDialog() {
		builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.delete_row_dialog, null);
		
		builder.setTitle("Remove Plot");
		builder.setMessage("Delete Plot?");
		builder.setView(layout);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				myPlots.remove(plotRow);
				plotsToDisplay();
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

	public class plotDisplayAdapter extends ArrayAdapter<Plot>{

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
			Plot currentPlot = myPlots.get(position);

			//fill the view
			//plot name
			TextView plot_name = (TextView) plotView.findViewById(R.id.plot_name);
			plot_name.setText(currentPlot.getName());

			//plot latitude
			TextView plot_lat = (TextView) plotView.findViewById(R.id.latitude);
			plot_lat.setText(currentPlot.getLatitude());

			//plot longitude
			TextView plot_long = (TextView) plotView.findViewById(R.id.longitude);
			plot_long.setText(currentPlot.getLongitude());

			//Configuration for the location/gps button on the List view
			final ImageButton location_button = (ImageButton) plotView.findViewById(R.id.pt_location);
			location_button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					switch(v.getId()){
					case R.id.pt_location:
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
				Bitmap img = (Bitmap) data.getExtras().get("data");

				myPlots.get(current_position).setImg(img);
				myPlots.get(current_position).save();

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
