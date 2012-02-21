package com.neolab.crm.client.fwk.containers;

import com.google.gwt.user.client.ui.FlowPanel;

public abstract class FlowContainer extends Container<FlowPanel>{
	public FlowContainer(boolean render) {
		super(new FlowPanel());
		if(render)
			render();
	}
}