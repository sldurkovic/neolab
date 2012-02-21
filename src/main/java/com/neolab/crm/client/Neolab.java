package com.neolab.crm.client;

import com.google.gwt.core.client.EntryPoint;
import com.neolab.crm.client.app.gin.Injector;

/**
 * Main entry point.
 *
 * @author Slobodan
 */
public class Neolab implements EntryPoint {
	
    public void onModuleLoad() {
    	Injector.INSTANCE.getApplication().run();
    }
}
