package com.neolab.crm.client.fwk;

import com.google.gwt.user.client.ui.Label;

public class Labels {
	
	public static Label getItalicLabel(String text){
		Label label = new Label(text);
		label.addStyleName("italic");
		return label;
	}

}
