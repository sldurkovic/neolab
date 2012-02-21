package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.app.base.Event;

public class TaskUpdatedEvent extends Event {
	
	private static final Logger log = Logger.getLogger(TaskUpdatedEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();
	

	public TaskUpdatedEvent() {
		super(TYPE);
	}

}
