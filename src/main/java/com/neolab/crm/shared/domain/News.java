package com.neolab.crm.shared.domain;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class News implements Serializable{
	
	private int nid;
	private String body;
	private Integer level;
	private Integer pid;
	private Date date;
	private String title;
	private Integer uid;
	private User user;
	
	public News() {
	}
	
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Integer getUid() {
		return uid;
	}


	public void setUid(Integer uid) {
		this.uid = uid;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "News [nid=" + nid + ", body=" + body + ", level=" + level
				+ ", pid=" + pid + ", date=" + date + ", title=" + title
				+ ", uid=" + uid + ", user=" + user + "]";
	}
	
	
}