package com.neolab.crm.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.view.client.ProvidesKey;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.shared.resources.rpc.Response;

@SuppressWarnings("serial")
public class ProjectActivity implements Serializable{
	
	private int paid;
	private Integer pid;
	private Integer uid;
	public ProjectActivity() {
		super();
	}
	
	public int getPaid() {
		return paid;
	}

	public void setPaid(int paid) {
		this.paid = paid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + paid;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectActivity other = (ProjectActivity) obj;
		if (paid != other.paid)
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}



	public static final ProvidesKey<ProjectActivity> KEY_PROVIDER = new ProvidesKey<ProjectActivity>() {
        public Object getKey(ProjectActivity item) {
          return item == null ? null : item.getPaid();
        }
      };
	@Override
	public String toString() {
		return "ProjectActivity [paid=" + paid + ", pid=" + pid + ", uid="
				+ uid + "]";
	}
      
}