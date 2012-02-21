package com.neolab.crm.client.app.widgets.hierarchy;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.Image;

public class NavButton extends CustomButton {
	
	public NavButton(Image upImage, Image downImage, ClickHandler handler) {
		super(upImage, downImage, handler);
		init();
	}

	public NavButton(Image upImage, Image downImage) {
		super(upImage, downImage);
		init();
	}

	private void init() {
		addStyleName("hand");
		Utils.hideFocusOutline(getElement());
	}

	public void setDown(boolean down) {
		super.setDown(down);
	}

	public boolean isDown() {
		return (super.isDown());
	}

	@Override
	protected void onClick() {
		if (!isDown())
			super.onClick();
	}
	
}
