package com.neolab.crm.client.app.widgets;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.fwk.containers.VerticalContainer;

public class ButtonDeck extends VerticalContainer{

	private DeckPanel deck;
	private HorizontalPanel bar;
	private ArrayList<ToggleButton> buttons;
	
	private VerticalPanel tasksDeck;
	private VerticalPanel newsDeck;
	private VerticalPanel calendarDeck;
	
	public ButtonDeck() {
		super(false);
		deck = new DeckPanel();
		bar = new HorizontalPanel();
		buttons = new ArrayList<ToggleButton>();
		render();
	}

	@Override
	protected void render() {
//		ToggleButton tasks = new ToggleButton(ImageFactory.toDo(), ImageFactory.toDo(), new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				deck.showWidget(1);
//				buttons.get(1).setDown(false);
//				buttons.get(2).setDown(false);
//			}
//		});
//		ToggleButton calendar = new ToggleButton(ImageFactory.calendar(), ImageFactory.calendar(), new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				deck.showWidget(2);
//				buttons.get(0).setDown(false);
//				buttons.get(2).setDown(false);
//			}
//		});
//		ToggleButton news = new ToggleButton(ImageFactory.documents(), ImageFactory.documents(), new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				deck.showWidget(0);
//				buttons.get(0).setDown(false);
//				buttons.get(1).setDown(false);
//			}
//		});
		
		ToggleButton tasks = new ToggleButton("Task list", "Task list", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deck.showWidget(1);
				buttons.get(1).setDown(false);
				buttons.get(2).setDown(false);
			}
		});
		ToggleButton calendar = new ToggleButton("Calendar", "Calendar", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deck.showWidget(2);
				buttons.get(0).setDown(false);
				buttons.get(2).setDown(false);
			}
		});
		ToggleButton news = new ToggleButton("News", "News", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deck.showWidget(0);
				buttons.get(0).setDown(false);
				buttons.get(1).setDown(false);
			}
		});
		
		tasks.addStyleName("neo-Toggle");
		calendar.addStyleName("neo-Toggle");
		news.addStyleName("neo-Toggle");
		
		buttons.add(tasks);
		buttons.add(calendar);
		buttons.add(news);

		bar.add(news);
		WidgetFactory.glue(bar);
		bar.add(tasks);
		WidgetFactory.glue(bar);
		bar.add(calendar);
		addWidget(bar);
		
		//deck
		tasksDeck = new VerticalPanel();
		
		newsDeck = new VerticalPanel();
		calendarDeck = new VerticalPanel();

		deck.add(newsDeck);
		deck.add(tasksDeck);
		deck.add(calendarDeck);
		tasks.setDown(true);
		deck.showWidget(1);
		deck.setStyleName("button-Deck");
		addWidget(deck);
		getTopContainer().setWidth("100%");
	}
	
	public void setTasksPanel(Widget w){
		tasksDeck.add(w);
	}
	
	public void setCalendarPanel(Widget w){
		calendarDeck.add(w);
	}
	
	public void setNewsPanel(Widget w){
		newsDeck.add(w);
	}
	
	
	
}
