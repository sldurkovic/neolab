package com.neolab.crm.shared.resources;

import java.io.Serializable;
import java.util.ArrayList;

import com.neolab.crm.shared.domain.Project;

@SuppressWarnings("serial")
public class ProjectPage implements Serializable{

	private Project project;
	private ArrayList<String> documents = new ArrayList<String>();
	private boolean member;
	
	public ProjectPage() {
	}
	
	public ProjectPage(Project project){
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ArrayList<String> getDocuments() {
		return documents;
	}

	public void setDocuments(ArrayList<String> documents) {
		this.documents = documents;
	}
	
	public void addDocument(String document){
		documents.add(document);
	}

	public boolean isMember() {
		return member;
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	@Override
	public String toString() {
		return "ProjectPage [project=" + project + ", documents=" + documents
				+ "]";
	}
	
	
}
