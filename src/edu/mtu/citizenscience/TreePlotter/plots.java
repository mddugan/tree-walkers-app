package edu.mtu.citizenscience.TreePlotter;

import android.graphics.Bitmap;

public class plots {

	private String name;
	private String latitude;
	private String longitude;
	private Bitmap img;
	
	public plots(String name, String latitude, String longitude, Bitmap img) {
		super();
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.img = img;
		
	}

	public Bitmap getImg() {
		return img;
	}

	public void setImg(Bitmap img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}


}
