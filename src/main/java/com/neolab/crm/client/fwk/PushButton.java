package com.neolab.crm.client.fwk;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.neolab.crm.client.fwk.containers.FlowContainer;

public class PushButton extends Composite implements HasClickHandlers, HasText, HasAllMouseHandlers {

	private FlexTable container;
	private Label left;
	private Label center;
	private Label right;
	
	public boolean hover;
	public boolean clicked;
	public boolean enabled = true;

	public PushButton() {
		this(null);
	}
	
	public PushButton(String text, ClickHandler handler) {
		this(text);
		addClickHandler(handler);
	}

	@UiConstructor
	public PushButton(String text) {
		container = new FlexTable();
		initWidget(container);
		container.setCellPadding(0);
		container.setCellSpacing(0);
		setStyleName("tae-Button");
		sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
		left = new Label();
		left.setStyleName("tae-Button-Left");
		container.setWidget(0, 0, left);
		container.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		center = new Label();
		center.setStyleName("tae-Button-Center");
		container.setWidget(0, 1, center);
		container.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		if(text != null ) {
			setText(text);
		}
		right = new Label();
		right.setStyleName("tae-Button-Right");
		container.setWidget(0, 2, right);
		container.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				hover = true;
				applyButtonStyle();
			}
		});
		
		addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				hover = false;
				applyButtonStyle();
			}
		});
		
		addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				clicked = true;
				applyButtonStyle();
			}
		});
		
		addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				clicked = false;
				applyButtonStyle();
			}
		});
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		if(!enabled) {
			return;
		}
		super.onBrowserEvent(event);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addHandler(handler, ClickEvent.getType());
	}

	@Override
	public String getText() {
		return center.getText();
	}

	@Override
	public void setText(String text) {
		center.setText(text);
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if(this.enabled != enabled) {
			this.enabled = enabled;
			applyButtonStyle();
		}
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());
	}
	
	private void applyButtonStyle() {
		if(!enabled) {
			setStylePrimaryName("tae-Button-Disabled");
			return;
		}
		if(clicked) {
			setStylePrimaryName("tae-Button-Down");
			return;
		}
		if(hover) {
			setStylePrimaryName("tae-Button-Hover");
			return;
		}
		setStylePrimaryName("tae-Button");
	}

}
