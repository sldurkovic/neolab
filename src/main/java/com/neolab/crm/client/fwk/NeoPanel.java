package com.neolab.crm.client.fwk;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.fwk.containers.FlowContainer;

/**
 * 
 * @author Slobodan
 * 
 * Flow panel container for all widget based panels
 */
public class NeoPanel extends FlowContainer{

	private FlowPanel content;

	public NeoPanel() {
		super(true);
	}
	
	public NeoPanel(Widget widget){
		this();
		addWidget(widget);
	}
	
	public void addWidget(Widget widget){
		if(widget != null)
			content.add(widget);
	}

	@Override
	protected void render() {
		getTopContainer().addStyleName("neo-Panel");
		content = new FlowPanel();
		content.addStyleName("neo-Panel-Content");
		getTopContainer().add(content);
	}

}
