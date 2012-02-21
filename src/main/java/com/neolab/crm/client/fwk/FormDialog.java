package com.neolab.crm.client.fwk;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.events.HideDialogsEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;

public class FormDialog extends DialogBox{

	private HorizontalPanel container;    
	private VerticalPanel contentContainer;
	private VerticalPanel content;
	private HorizontalPanel buttonBar;    
//	private Image x;
	
	public FormDialog() {
		init();
		render();		
		center();
	}

	private void init() {
		container = new HorizontalPanel();
		contentContainer = new VerticalPanel();
		addStyleName("formDialog");
		setWidget(container);
		//options
		setGlassEnabled(true);
		setAnimationEnabled(true);
		Injector.INSTANCE.getEventBus().addHandler(HideDialogsEvent.TYPE, new HideDialogsEvent.Handler<HideDialogsEvent>() {
			@Override
			public void on(HideDialogsEvent e) {
				hide();
		}});
	}
	
	private void render() {
		//init caption
		content = new VerticalPanel();
		contentContainer.setStyleName("formDialog-ContentContainer");
		Panel cross = getCross();
		contentContainer.add(content);
		container.add(contentContainer);
		container.setCellHorizontalAlignment(content, HasHorizontalAlignment.ALIGN_CENTER);
		container.add(cross);
		
		buttonBar = new HorizontalPanel();
		buttonBar.addStyleName("formDialog-Buttons");
		contentContainer.add(buttonBar);
		contentContainer.setCellHorizontalAlignment(buttonBar, HasHorizontalAlignment.ALIGN_CENTER);
	}

	public void addWidget(Widget w) {
		content.add(w);
		content.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_CENTER);
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 100);

	}
	
	public void addWidget(Widget w, HorizontalAlignmentConstant align) {
		content.add(w);
		content.setCellHorizontalAlignment(w, align);
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}
	@Override
	public void setWidth(String width) {
		content.setWidth(width);
	}
	
	protected VerticalPanel getContent(){
		return content;
	}
	
	public void addButton(String label, ClickHandler handler){
		Panel button = WidgetFactory.getBigButton(label, handler);
		if(buttonBar.getWidgetCount() > 0)
			WidgetFactory.glue(buttonBar);
		buttonBar.add(button);
	}
	
	private Panel getCross(){
		SimplePanel cross = new SimplePanel();
		cross.sinkEvents(Event.ONCLICK);
		cross.setStyleName("square-cross");
		cross.addStyleName("hand");
		cross.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		}, ClickEvent.getType());
		return cross;
	}
}
