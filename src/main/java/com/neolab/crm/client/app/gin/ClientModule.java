package com.neolab.crm.client.app.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.neolab.crm.client.app.Application;
import com.neolab.crm.client.app.ApplicationShell;
import com.neolab.crm.client.app.ApplicationShellImpl;
import com.neolab.crm.client.app.i18n.Constants;
import com.neolab.crm.client.mvp.view.ViewFactory;

public class ClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(Application.class).in(Singleton.class);
		bind(ApplicationShell.class).to(ApplicationShellImpl.class).in(Singleton.class);
		bind(Constants.class).in(Singleton.class);
		bind(ViewFactory.class).in(Singleton.class);
	}

}
