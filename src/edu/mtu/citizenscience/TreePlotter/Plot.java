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
	
	public Plot(Context ctx){
		super(ctx);
	}
	
	public Plot(Context ctx, String user ,String name, String latitude, String longitude, Bitmap img) {
		super(ctx);
		this.user = user;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.img = img;
		
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


}
