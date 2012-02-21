package com.neolab.crm.client.app.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.neolab.crm.client.app.Application;
import com.neolab.crm.client.app.ApplicationShell;
import com.neolab.crm.client.app.base.AppPlaceController;
import com.neolab.crm.client.app.i18n.Constants;
import com.neolab.crm.client.mvp.view.ViewFactory;
import com.neolab.crm.shared.services.NeoServiceAsync;

@GinModules({SystemModule.class, ClientModule.class})
public interface Injector extends Ginjector {
	
	static final Injector INSTANCE = GWT.create(Injector.class);
	
	Application getApplication();
	ApplicationShell getApplicationShell();
	NeoServiceAsync getNeoService();
	AppPlaceController getAppPlaceController();
	EventBus getEventBus();
	Constants getConstants();
	ViewFactory getViewFactory();
}

