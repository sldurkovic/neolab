package com.neolab.crm.client.app;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.widgets.NeoTabPanel;
import com.neolab.crm.shared.domain.User;

public interface ApplicationShell {

	Widget getDisplay();
	NeoTabPanel getNeoTabPanel();
	SimpleLayoutPanel getLoginShell();
	void showLoginShell();
	void showAppShell();
	void constructBar(User object);
}
