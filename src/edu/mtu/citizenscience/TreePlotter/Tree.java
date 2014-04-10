package edu.mtu.citizenscience.TreePlotter;

import com.orm.SugarRecord;

import android.content.Context;
import android.graphics.Bitmap;

public class Tree extends SugarRecord<Tree>{

	private String plot;
	private String name;
	private String abundance;
	private String size;
	private Bitmap img;
	
	
	public Tree(Context ctx){
		super(ctx);
	}
	
	public Tree(Context ctx, String plot, String name, String abundance, String size, Bitmap img) {
		super(ctx);
		this.plot = plot;
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
		return plot;
	}

	public void setPlot_name(String plot) {
		this.plot = plot;
	}
	
}
