package com.neolab.crm.client.app.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.shared.resources.rpc.Response;

public class WidgetFactory {

	// private Panel getCross(ClickHandler handler){
	// SimplePanel cross = new SimplePanel();
	// cross.sinkEvents(Event.ONCLICK);
	// cross.setStyleName("square-cross");
	// cross.addStyleName("hand");
	// cross.addHandler(handler, ClickEvent.getType());
	// return cross;
	// }

	public static Panel getBigButton(String string, ClickHandler handler) {
		SimplePanel panel = new SimplePanel();
		panel.add(new Label(string));
		panel.sinkEvents(Event.ONCLICK);
		panel.setStyleName("big-Button");
		panel.addStyleName("hand");
		panel.addHandler(handler, ClickEvent.getType());
		return panel;
	}
	
	public static HorizontalPanel getClickablePanel(ClickHandler handler){
		HorizontalPanel panel = new HorizontalPanel();
		panel.sinkEvents(Event.ONCLICK);
		panel.addStyleName("hand");
		panel.addHandler(handler, ClickEvent.getType());
		return panel;
	}

	public static void glue(HorizontalPanel panel) {
		HTML glue = new HTML("&nbsp;");
		panel.add(glue);
		panel.setCellWidth(glue, "10px");
	}
	
	public static void glue(HorizontalPanel panel, String width, String separator) {
		if(separator == null){
			HTML glue = new HTML("&nbsp;");
			panel.add(glue);
			panel.setCellWidth(glue, width);
		}else{
			Label glue = new Label(separator);
			panel.add(glue);
			panel.setCellHorizontalAlignment(glue, HasHorizontalAlignment.ALIGN_CENTER);
			panel.setCellWidth(glue, width);
		}
	}
	
	public static void glue(VerticalPanel panel, String height) {
		HTML glue = new HTML("&nbsp;");
		panel.add(glue);
		panel.setCellHeight(glue, height);
	}

	public static void alert(String text) {
//		Window.alert(text);
		final FormDialog alert = new FormDialog();
		alert.setGlassStyleName("glass-Max-Level");
		alert.addStyleName("z-Index-Max");
		alert.setWidth("150px");
		Label info = new Label("Info");
		info.addStyleName("formDialog-Light-Title");
		Label l = new Label(text);
		alert.addWidget(info, HasHorizontalAlignment.ALIGN_LEFT);
		alert.addWidget(l, HasHorizontalAlignment.ALIGN_LEFT);
		alert.addButton("OK", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				alert.hide();
			}
		});
//		alert.addButton("Close", new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				alert.hide();
//			}
//		});
	}

	public static void info(String text) {
//		Window.alert(text);
		final FormDialog alert = new FormDialog();
		alert.setGlassStyleName("glass-Max-Level");
		alert.addStyleName("z-Index-Max");
		alert.setWidth("150px");
		Label info = new Label("Alert");
		info.addStyleName("formDialog-Light-Title");
		Label l = new Label(text);
		alert.addWidget(info, HasHorizontalAlignment.ALIGN_LEFT);
		alert.addWidget(l, HasHorizontalAlignment.ALIGN_LEFT);
//		alert.addButton("Close", new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				alert.hide();
//			}
//		});
	}	

	public static void error(String text) {
//		Window.alert(text);
		final FormDialog alert = new FormDialog();
		alert.setGlassStyleName("glass-Max-Level");
		alert.addStyleName("z-Index-Max");
		alert.setWidth("150px");
		Label info = new Label("Error");
		info.addStyleName("formDialog-Light-Title");
		info.addStyleName("red-Label");
		Label l = new Label(text);
		alert.addWidget(info, HasHorizontalAlignment.ALIGN_LEFT);
		alert.addWidget(l, HasHorizontalAlignment.ALIGN_LEFT);
		alert.addButton("OK", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				alert.hide();
			}
		});
//		alert.addButton("Close", new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				alert.hide();
//			}
//		});
	}

	public static void notify(Response result) {
		if (result.getStatus()) {
			info(result.getMsg());
		} else {
			error(result.getMsg());
		}
	}

	public static Label getStatusLabel(String status) {
		Label label = new Label(status);
		if (status.equals("ACTIVE"))
			label.addStyleName("red-Label");
		if (status.equals("FINISHED"))
			label.addStyleName("green-Label");
		return label;
	}
	
	public static Label getInternalHyperlink(String title, final Place place) {
		Label label = new Label(title);
		label.addStyleName("hiperlink");
		label.addStyleName("display-InlineBlock");
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(place));
			}
		});
		return label;
	}

	
	public static HTML getHyperlink(String title, String href, boolean blank) {
		HTML html;
		if (blank) {
			html = new HTML("<a href=\"http://" + href
					+ "\" target=\"_blank\">" + title + "</a>");
		} else {
			html = new HTML("<a href=\"http://" + href
					+ "\">" + title + "</a>");
		}
		html.addStyleName("hiperlink");
		return html;
	}

	public static int confirm(String question) {
		return 0;
	}

	public static void addPanelClick(Panel panel,
			ClickHandler clickHandler) {
		panel.sinkEvents(Event.ONCLICK);
		panel.addHandler(clickHandler, ClickEvent.getType());
		
	}

}
