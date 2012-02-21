package com.neolab.crm.shared.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class Task implements Serializable{
	
	
	private int tid;
	private String title;
	private Integer pid;
	private Date dateStart;
	private Date dateEnd;
	private String description;
	private String status;
	private Project project;
	
	
	public Task() {
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "Task [tid=" + tid + ", title=" + title + ", pid=" + pid
				+ ", dateStart=" + dateStart + ", dateEnd=" + dateEnd
				+ ", description=" + description + ", status=" + status + "]";
	}


	public static final ProvidesKey<Task> KEY_PROVIDER = new ProvidesKey<Task>() {
        public Object getKey(Task item) {
          return item == null ? null : item.getTid();
        }
      };
}