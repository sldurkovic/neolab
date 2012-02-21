package com.neolab.crm.client.app.widgets.projects;

import java.util.ArrayList;

import com.neolab.crm.shared.domain.Project;

public class UserCategoryProjects{
	private int cat;
	private ArrayList<Project> projects;
	
	public UserCategoryProjects(int cat) {
		this.cat = cat;
	}
	
	public UserCategoryProjects(int cat, ArrayList<Project> projects) {
		this.cat = cat;
		this.projects = projects;
	}
	public int getCat() {
		return cat;
	}
	public void setCat(int cat) {
		this.cat = cat;
	}
	public ArrayList<Project> getProjects() {
		return projects;
	}
	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	@Override
	public String toString() {
		return "UserCategoryProjects [cat=" + cat + ", projects=" + projects
				+ "]";
	}
	
}