package edu.mtu.citizenscience.TreePlotter;

import android.graphics.Bitmap;

public class LargeTree {

	private String name;
	private String abundance;
	private Bitmap img;
	
	public LargeTree(String name, String abundance, Bitmap img) {
		super();
		this.name = name;
		this.abundance = abundance;
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

	public String getAbundance() {
		return abundance;
	}
	
	

}
