package edu.mtu.citizenscience.TreePlotter;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PlotTableActivity extends Activity {

	ArrayList<plots> myPlots = new ArrayList<plots>();
	private AlertDialog.Builder builder;
	private AlertDialog ad;
	private  String plot_name = "plot name" , plot_lat = "latitude", plot_long = "longitude";
	private final int CAMERA_INTENT_CODE = 7;
	private int current_position = -1;
	private EditText input_plot_lat;
	private EditText input_plot_long;
	private LocationManager lManager;
	private LocationListener gListener;
	private Location bestLocation;
	private AsyncTask<Void,Void,Void> currentGPSTasker;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_table);
		android.app.ActionBar actionBar = getActionBar();
		actionBar.setTitle("Plot Table");
		actionBar.show();

		//set up LocationManager and Listener for GPS
		currentGPSTasker = null;
		//sets up a location Manager
		lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		bestLocation = lManager.getLastKnownLocation(lManager.GPS_PROVIDER);


		//set up Location listener
		gListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				Log.d("GPS", "location has changed");
				if (bestLocation == null) {
					Log.d("GPS", "setting location for the first time");
					bestLocation = location;
				}
				else { //compare them
					//check that it is newer than bestLocation
					if (location.getTime() - bestLocation.getTime() > 0 ) {
						Log.d("GPS", "new location is new than previous location");
						//check that it is more accurate
						if (location.getAccuracy() < bestLocation.getAccuracy()) {
							Log.d("GPS", "new location is more accurate, setting");
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
		input_plot_lat = (EditText)layout.findViewById(R.id.np_plot_lat);
		input_plot_long = (EditText) layout.findViewById(R.id.np_plot_long);



		plot_name = "plot name";
		plot_lat = "latitude";
		plot_long = "longitude";

		builder.setTitle("New Plot");
		builder.setMessage("Enter the plot name and coorinates");
		builder.setView(layout);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				plot_name =  input_plot_name.getText().toString();
				plot_lat = input_plot_lat.getText().toString();
				plot_long = input_plot_long.getText().toString();

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

	private class GPSUpdaterTask extends AsyncTask<Void, Void, Void> {
		private final float MAX_INACCURACY = 100; //in meters
		private final long MAX_ELAPSED_TIME = 1000 * 60 * 2;
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
		protected Void doInBackground(Void... params) {
			startTime = System.currentTimeMillis();
			long currentTime = -1;
			//wait for more recent location update
			while(updated.getTime() <= bestLocation.getTime()) {
				currentTime = System.currentTimeMillis();
				if (currentTime - startTime > MAX_ELAPSED_TIME) {
					Log.d("GPS", "GPS timeout");
					return null;
				}
				updated = lManager.getLastKnownLocation(provider);
			}

			//now wait for an accurate one
			while(updated.getAccuracy() > MAX_INACCURACY) {
				currentTime = System.currentTimeMillis();
				if (currentTime - startTime > MAX_ELAPSED_TIME) {
					Log.d("GPS", "GPS timeout");
					return null;
				}
				updated = lManager.getLastKnownLocation(provider);
			}

			Log.d("GPS", "location is accurate enough");

			//set new bestLocation
			bestLocation = updated;

			//stop listening
			lManager.removeUpdates(gListener);

			Log.d("GPS", "turned off location updates");

			return null;
		}

		protected void onPostExecute(Void v) {
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
			input_plot_long.setText(Double.toString(bestLocation.getLongitude()));

		}

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
		}
		else {
			if (currentGPSTasker.getStatus() == AsyncTask.Status.FINISHED){
				currentGPSTasker = new GPSUpdaterTask().execute(new Void[1]);
			}
			else {
				Log.d("GPS", "GPS Thread already running");
			}
		}

	}

	private void plotToList(String name, String latitude, String longitude) {
		myPlots.add(new plots(name, latitude, longitude, null));
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
