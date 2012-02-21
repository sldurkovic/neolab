package com.neolab.crm.client.app.widgets;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.events.SwitchTabEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.utils.Util;

public class NeoTabPanel extends Composite{

	private final Logger log = Logger.getLogger(getClass().getName());
	
	private TabLayoutPanel container;
	private AcceptsOneWidget content;
	private ArrayList<AcceptsOneWidget> tabContentContainers;
	
	public NeoTabPanel(int size, Unit unit){
		container = new TabLayoutPanel(size, unit);
		tabContentContainers = new ArrayList<AcceptsOneWidget>();
		initWidget(container);
		initComponents();
	}

	private void initComponents() {
		addTab(new HTML(""), Injector.INSTANCE.getConstants().home());
		addTab(new HTML(""), Injector.INSTANCE.getConstants().projects());
//		addTab(new HTML(""), Injector.INSTANCE.getConstants().documents());
		addTab(new HTML(""), Injector.INSTANCE.getConstants().members());
		container.setAnimationDuration(300);
		container.setStyleName("master-TabPanel");
		
		container.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Injector.INSTANCE.getEventBus().fireEvent(
						new GoToPlaceEvent(Injector.INSTANCE.getApplication().getPlaceById(event.getSelectedItem())));
			}
		});
		
		Injector.INSTANCE.getEventBus().addHandler(SwitchTabEvent.TYPE, new SwitchTabEvent.Handler<SwitchTabEvent>(){

			@Override
			public void on(SwitchTabEvent e) {
//				Util.logFine(log, "Switching tab: "+e.getTabId());
				container.selectTab(e.getTabId(), false);
			}
			
		});
		content = new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				tabContentContainers.get(container.getSelectedIndex()).setWidget(w);
			}
		};
	}
	
	/**
	 * registers container in activity
	 */
	public void addTab(Widget widget, String title){
		SimplePanel wrapper = new SimplePanel();
		wrapper.setWidget(widget);
		container.add(wrapper, title);
		tabContentContainers.add(wrapper);
	}

	public AcceptsOneWidget getContentWidget(){
		return content;
	}

}
