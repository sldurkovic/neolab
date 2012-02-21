package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.app.base.Event;

public class TaskAssignedEvent extends Event {
	
	private static final Logger log = Logger.getLogger(TaskAssignedEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();
	

	public TaskAssignedEvent() {
		super(TYPE);
	}

}
