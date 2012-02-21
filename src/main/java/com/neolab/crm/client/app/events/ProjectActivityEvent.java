package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.app.base.Event;

public class ProjectActivityEvent extends Event {
	
	private static final Logger log = Logger.getLogger(ProjectActivityEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();

	private int pid;

	private boolean join;
	
	public ProjectActivityEvent(int pid, boolean join) {
		super(TYPE);
		this.pid = pid;
		this.join = join;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
	}

}
