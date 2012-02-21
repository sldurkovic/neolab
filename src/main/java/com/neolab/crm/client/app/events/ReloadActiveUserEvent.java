package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.neolab.crm.client.app.base.Event;
import com.neolab.crm.client.utils.Util;

public class ReloadActiveUserEvent extends Event {
	private static final Logger log = Logger.getLogger(ReloadActiveUserEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();

	public ReloadActiveUserEvent() {
		super(TYPE);
		Util.logFine(log,"[Event] ReloadUser");
	}


}
