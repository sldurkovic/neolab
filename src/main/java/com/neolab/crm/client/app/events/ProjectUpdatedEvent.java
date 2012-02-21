package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.app.base.Event;

public class ProjectUpdatedEvent extends Event {
	
	private static final Logger log = Logger.getLogger(ProjectUpdatedEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private int pid;

	public ProjectUpdatedEvent(int pid) {
		super(TYPE);
		this.pid = pid;
	}

	public int getPid(){
		return pid;
	}

}
