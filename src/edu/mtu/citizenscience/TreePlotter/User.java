package edu.mtu.citizenscience.TreePlotter;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class User extends SugarRecord<User>{
	
	private String username;
	private String email;
	private String skill_level;
	private List<Plot> user_plots;
	
	public User(Context context){
		super(context);
	}
	
	public User(Context context, String username, String email, String skill_level, List<Plot> user_plots) {
		super(context);
		this.username = username;
		this.email = email;
		this.skill_level = skill_level;
		this.user_plots = user_plots;
	}
	
	public List<Plot> getUser_plots() {
		return user_plots;
	}

	public void setUser_plots(List<Plot> user_plots) {
		this.user_plots = user_plots;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSkill_level() {
		return skill_level;
	}
	public void setSkill_level(String skill_level) {
		this.skill_level = skill_level;
	}
	
	

}
