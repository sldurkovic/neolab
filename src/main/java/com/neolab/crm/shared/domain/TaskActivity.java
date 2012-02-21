package com.neolab.crm.shared.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class TaskActivity implements Serializable{
	
	private int taid;
	
	private int tid;
	private int uid;
	private Task task;
	
	public TaskActivity() {
	}
	
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public int getTaid() {
		return taid;
	}
	public void setTaid(int taid) {
		this.taid = taid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public static final ProvidesKey<TaskActivity> KEY_PROVIDER = new ProvidesKey<TaskActivity>() {
        public Object getKey(TaskActivity item) {
          return item == null ? null : item.getTaid();
        }
      };

	@Override
	public String toString() {
		return "TaskActivity [taid=" + taid + ", tid=" + tid + ", uid=" + uid
				+ "]";
	}

}