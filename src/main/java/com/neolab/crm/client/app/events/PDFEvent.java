package com.neolab.crm.client.app.events;

import java.util.logging.Logger;

import com.neolab.crm.client.app.base.Event;

public class PDFEvent extends Event {
	
	private static final Logger log = Logger.getLogger(PDFEvent.class.getName());

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private int pid;
	private int uid;

	public PDFEvent(int pid, int uid) {
		super(TYPE);
		this.pid = pid;
		this.uid = uid;
	}

	public int getProject(){
		return pid;
	}
	
	public int getUser(){
		return uid;
	}

}
