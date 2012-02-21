package com.neolab.crm.shared.domain;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class Category implements Serializable{
	
	private int cid;
	private String title;

	public Category() {
	}
	
	public int getCid() {
		return cid;
	}
	
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "Category [cid=" + cid + ", title=" + title + "]";
	}
	
}