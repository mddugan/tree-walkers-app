package edu.mtu.citizenscience.TreePlotter;

import android.graphics.Bitmap;

public class plots {

	private String name;
	private String coordinates;
	private Bitmap img;
	
	public plots(String name, String coordinates, Bitmap img) {
		super();
		this.name = name;
		this.coordinates = coordinates;
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

	public String getCoordinates() {
		return coordinates;
	}
	

}
