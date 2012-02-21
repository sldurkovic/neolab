package com.neolab.crm.shared.domain;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class Project implements Serializable{
	
	private int pid;
	private String title;
	private Integer cid;
	private Integer createdBy;
	private Date dateCreated;
	private String description;
	
	public Project() {
	}
	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}
	
	
	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}



	public static final ProvidesKey<Project> KEY_PROVIDER = new ProvidesKey<Project>() {
        public Object getKey(Project item) {
          return item == null ? null : item.getPid();
        }
      };

	@Override
	public String toString() {
		return "Project [pid=" + pid + ", title=" + title + ", cid=" + cid
				+ ", createdBy=" + createdBy + ", dateCreated=" + dateCreated
				+ "]";
	}

	
      
}