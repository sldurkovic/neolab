package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.app.base.Event;

public class ProjectUserChangeEvent extends Event {
	
	private static final Logger log = Logger.getLogger(ProjectUserChangeEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private int pid;

	public ProjectUserChangeEvent(int pid) {
		super(TYPE);
		this.pid = pid;
	}

	public int getProject(){
		return pid;
	}

}
