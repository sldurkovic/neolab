package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.neolab.crm.client.app.base.Event;
import com.neolab.crm.shared.domain.User;

public class UserDeliveryEvent extends Event {
	
	private static final Logger log = Logger.getLogger(UserDeliveryEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private User user;

	public UserDeliveryEvent(User user) {
		super(TYPE);
		this.user = user;
		log.fine("UserDelivered: "+user);
	}

	public User getUser() {
		return user;
	}

}
