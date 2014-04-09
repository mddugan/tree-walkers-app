package edu.mtu.citizenscience.TreePlotter;

import android.graphics.Bitmap;

public class Tree {

	private String plot_name;
	private String name;
	private String abundance;
	private String size;
	private Bitmap img;
	
	public Tree(String plot_name, String name, String abundance, String size, Bitmap img) {
		super();
		this.plot_name = plot_name;
		this.name = name;
		this.abundance = abundance;
		this.size = size;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPlot_name() {
		return plot_name;
	}

	public void setPlot_name(String plot_name) {
		this.plot_name = plot_name;
	}
	
}
