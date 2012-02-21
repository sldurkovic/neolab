package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.app.base.Event;

public class NewProjectEvent extends Event {
	
	private static final Logger log = Logger.getLogger(NewProjectEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private int cid;

	public NewProjectEvent(int cid) {
		super(TYPE);
		this.cid = cid;
	}

	public int getCategory(){
		return cid;
	}

}
