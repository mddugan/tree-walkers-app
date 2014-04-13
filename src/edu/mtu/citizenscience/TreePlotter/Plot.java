package edu.mtu.citizenscience.TreePlotter;

import com.orm.SugarRecord;

import android.content.Context;
import android.graphics.Bitmap;

public class Plot extends SugarRecord<Plot>{

	private String user;
	private String name;
	private String latitude;
	private String longitude;
	private Bitmap img;
	private boolean upload;
	
	public Plot(Context ctx){
		super(ctx);
	}
	
	public Plot(Context ctx, String user ,String name, String latitude, String longitude, Bitmap img, boolean upload) {
		super(ctx);
		this.user = user;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.img = img;
		this.upload = upload;
		
	}


	public String getUser() {
		return user;
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

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	

}
