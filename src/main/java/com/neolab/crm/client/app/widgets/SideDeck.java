package com.neolab.crm.client.app.widgets;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.fwk.containers.HorizontalContainer;
import com.neolab.crm.client.mvp.activities.HomeActivity;

public class SideDeck extends HorizontalContainer {

	// private FlexTable sidebar;
	private VerticalPanel sidebar;
	private DeckPanel deck;
	private ArrayList<SideLink> links;
	private HomeActivity presenter;

	public SideDeck(HomeActivity presenter) {
		super(false);
		this.presenter = presenter;
		render();
	}

	@Override
	protected void render() {
		// sidebar = new FlexTable();
		sidebar = new VerticalPanel();
		sidebar.setWidth("100%");
		sidebar.addStyleName("user-Sidebar");
		deck = new DeckPanel();
		links = new ArrayList<SideLink>();
		addWidget(sidebar);
		addWidget(deck);
		addWidget(new RightSidebar(presenter));
		getTopContainer().setCellWidth(deck, "100%");
	}

	public void addToSidebar(Widget w) {
		// sidebar.setWidget(sidebar.getRowCount(), 0, w);
		sidebar.add(w);
		sidebar.setCellWidth(w, "100%");
		sidebar.setCellHorizontalAlignment(w,
				HasHorizontalAlignment.ALIGN_CENTER);
	}

	/**
	 * @param linkName
	 *            in the sidebar
	 * @param w
	 *            widget to put in the center
	 */
	public void addView(String linkName, Widget w) {
		deck.add(w);
		w.addStyleName("side-View");
		final int index = deck.getWidgetIndex(w);
		final SideLink s = new SideLink(linkName, index) {
			public void onClick() {
				deck.showWidget(index);
				for (SideLink s : links) {
					s.unselect();
				}
			};
		};
		links.add(s);
		// sidebar.setWidget(sidebar.getRowCount(), 0, s);
		addToSidebar(s);
		if (index == 0)
			selectView(0);
	}

	public void selectView(int id) {
		for (SideLink s : links) {
			if (s.getDeckId() == id) {
				s.onClick();
				s.select();
			}
		}
	}

	private class SideLink extends HorizontalContainer {

		private int deckId;
		private Label label;
		private SimplePanel image;

		public SideLink(String label, int deckId) {
			super(false);
			this.label = new Label(label);
			image = new SimplePanel();
			// l.addClickHandler(new ClickHandler() {
			//
			// @Override
			// public void onClick(ClickEvent event) {
			// SideLink.this.onClick();
			// }
			this.deckId = deckId;
			render();
		}

		@Override
		protected void render() {
			getTopContainer().setStyleName("side-Link");
			getTopContainer().sinkEvents(Event.ONCLICK);
			getTopContainer().setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			getTopContainer().addHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					SideLink.this.onClick();
					select();
//					image.clear();
//					image.setWidget(ImageFactory.projectSmallIco());
				}
			}, ClickEvent.getType());
			image.setWidget(ImageFactory.classSmall());
			addWidget(image);
			getTopContainer().setCellWidth(image, "19px");
			addWidget(this.label);
		}

		public int getDeckId() {
			return deckId;
		}

		public void onClick() {
		}

		public void select() {
			addStyleName("side-Link-Selected");
		}

		public void unselect() {
			removeStyleName("side-Link-Selected");
		}

	}

}
