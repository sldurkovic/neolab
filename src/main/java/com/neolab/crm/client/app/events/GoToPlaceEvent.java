package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.app.base.Event;

public class GoToPlaceEvent extends Event {
	
	private static final Logger log = Logger.getLogger(GoToPlaceEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Place place;

	public GoToPlaceEvent(Place place) {
		super(TYPE);
		this.place = place;
//		log.fine("GoToPlaceEvent");
	}

	public Place getPlaceToGo() {
		return place;
	}

}
