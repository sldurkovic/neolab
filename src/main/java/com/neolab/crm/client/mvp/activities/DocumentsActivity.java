package com.neolab.crm.client.mvp.activities;

import java.util.logging.Logger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.events.SwitchTabEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.mvp.BasePresenter;
import com.neolab.crm.client.mvp.view.DocumentsView;
import com.neolab.crm.client.utils.Util;

public class DocumentsActivity extends BasePresenter<DocumentsView>{
	
	private static final Logger log = Logger.getLogger(DocumentsActivity.class.getName());
	
	public DocumentsActivity() {
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Util.logFine(log, "Starting activity CALENDAR");
		Injector.INSTANCE.getEventBus().fireEvent(new SwitchTabEvent(2));
		setDisplay(new DocumentsView(this));
		panel.setWidget(display);
	}


}
