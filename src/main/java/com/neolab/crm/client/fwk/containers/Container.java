package com.neolab.crm.client.fwk.containers;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public abstract class Container<E extends Panel> extends Composite{

	protected E container;
	
	public Container(E container){
		this.container = container;
		initWidget(this.container);
	}
	
	public void addWidget(Widget w){
		container.add(w);
	}
	
	protected E getTopContainer(){
		return container;
	}
	
	protected void clear(){
		container.clear();
	}
	protected abstract void render();
	
}
