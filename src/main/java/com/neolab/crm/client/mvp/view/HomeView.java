package com.neolab.crm.client.mvp.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.CalendarWidget;
import com.neolab.crm.client.app.widgets.NewsStream;
import com.neolab.crm.client.app.widgets.ProfileWidget;
import com.neolab.crm.client.app.widgets.ProfileWidget.UserProvider;
import com.neolab.crm.client.app.widgets.SideDeck;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.fwk.ConfirmDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.mvp.BaseView;
import com.neolab.crm.client.mvp.activities.HomeActivity;
import com.neolab.crm.shared.domain.News;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.TablePage;

public class HomeView extends BaseView<HomeActivity>  implements Requires<User>{

	private HorizontalPanel container;
	
	private SideDeck sideDeck;
	private ProfileWidget profileWidget;
	private SimplePanel imageContainer;
	
	public HomeView(HomeActivity p) {
		super(p);
		render();
	}

	@Override
	public void render() {
		sideDeck = new SideDeck(presenter);
		imageContainer = new SimplePanel();
		imageContainer.addStyleName("user-Profile-Image");
		container = new HorizontalPanel();
		container.setWidth("100%");
		container.add(sideDeck);
		container.setCellWidth(sideDeck,"100%");
		getTopContainer().add(container);
		getTopContainer().setCellWidth(container,"100%");
		getTopContainer().setWidth("100%");
		sideDeck.addToSidebar(imageContainer);
		sideDeck.addView("News feed",  new NewsStream(new NewsStream.FeedProvider() {
			
			@Override
			public void getFeed(int level, int start, int end,
					Requires<TablePage<News>> caller) {
				presenter.getFeed(level, 0, start, end, caller);
			}
		}));
		
		profileWidget = new ProfileWidget(Injector.INSTANCE.getApplication().getActiveUser(), new UserProvider() {
			@Override
			public void updateUser(User user) {
				presenter.updateUser(user);
			}

			@Override
			public void requestUser(Requires<User> object) {
				presenter.requestUser(object);
			}
		});
//		RootLayoutPanel.get().clear();
//		RootLayoutPanel.get().add(new TimelineWidget(Injector.INSTANCE.getApplication().getActiveUser().getUid()));
		sideDeck.addView("My profile", profileWidget);
		sideDeck.addView("Calendar", new CalendarWidget(-1, true));
		sideDeck.addToSidebar(new Label(""));
//		initSidebar();
//		initCentral();
//		initCalendar();
	}

//	private void initCalendar() {
//		VerticalPanel sidebar = new VerticalPanel();
//		addWidget(sidebar);
//		DatePicker picker = new DatePicker();
//		sidebar.add(picker);
//		Label t = new Label("Todays tasks");
//		Label t1 = new Label("Task 1");
//		Label t2 = new Label("Task 2");
//		Label t3 = new Label("Task 3");
//		sidebar.add(t);
//		sidebar.add(t1);
//		sidebar.add(t2);
//		sidebar.add(t3);
//		container.setCellHorizontalAlignment(sidebar, HasHorizontalAlignment.ALIGN_RIGHT);
//	}
//
//	private void initCentral() {
//		VerticalPanel central = new VerticalPanel();
//		central.add(new Label("Home feed"));
//		central.setWidth("600px");
//		addWidget(central);
//	}
	
	public void addWidget(Widget w) {
		container.add(w);
	}

	public HasAsyncInformation getProfileWidget() {
		return profileWidget;
	}

	@Override
	public void delivery(User object) {
		imageContainer.clear();
		imageContainer.add(ImageFactory.getUserImage(object.getImage()));
//		WaitPanel.wait(false);
	}

}
