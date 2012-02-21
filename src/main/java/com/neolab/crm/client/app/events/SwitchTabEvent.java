package com.neolab.crm.client.app.events;

import com.neolab.crm.client.app.base.Event;

public class SwitchTabEvent extends Event {
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private int tab;

	public SwitchTabEvent(int tab) {
		super(TYPE);
		this.tab = tab;
	}

	public int getTabId() {
		return tab;
	}

}
