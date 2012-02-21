package com.neolab.crm.shared.resources;

import java.io.Serializable;
import java.util.HashMap;

import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
public class Configuration implements Serializable{
	
	private String projectsPath;
	
	private Privileges privileges;
	
	public Configuration() {
	}

	public String getProjectsPath() {
		return projectsPath;
	}

	public void setProjectsPath(String projectsPath) {
		this.projectsPath = projectsPath;
	}

	public Privileges getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Privileges privileges) {
		this.privileges = privileges;
	}
	
}
