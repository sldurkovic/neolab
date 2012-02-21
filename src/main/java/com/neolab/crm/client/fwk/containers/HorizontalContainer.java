package com.neolab.crm.client.fwk.containers;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public abstract class HorizontalContainer extends Container<HorizontalPanel>{
	public HorizontalContainer(boolean render) {
		super(new HorizontalPanel());
		if(render)
			render();
	}
}
