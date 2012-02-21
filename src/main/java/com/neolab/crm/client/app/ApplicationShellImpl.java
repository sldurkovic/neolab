package com.neolab.crm.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.NeoTabPanel;
import com.neolab.crm.client.mvp.places.HomePlace;
import com.neolab.crm.shared.domain.User;

public class ApplicationShellImpl extends Composite implements ApplicationShell {

	private static ApplicationShellImplUiBinder uiBinder = GWT
			.create(ApplicationShellImplUiBinder.class);

	interface ApplicationShellImplUiBinder extends
			UiBinder<Widget, ApplicationShellImpl> {
	}

	@UiField
	SimpleLayoutPanel layout;
	
	private HorizontalPanel userBar;
	private SimpleLayoutPanel loginShell;
	private NeoTabPanel neoTabPanel;
	private Image logo;
	
	public ApplicationShellImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		neoTabPanel = new NeoTabPanel(40, Unit.PX);
		loginShell = new SimpleLayoutPanel();
		layout.add(neoTabPanel);
		logo = ImageFactory.neologo();
		logo.addStyleName("neologo");
		logo.setVisible(false);
		RootPanel.get().add(logo);
		userBar = new HorizontalPanel();
		userBar.setVisible(false);
		RootPanel.get().add(userBar);
	}

	public Widget getDisplay() {
		return this;
	}
	
	@Override
	public NeoTabPanel getNeoTabPanel() {
		return neoTabPanel;
	}

	@Override
	public SimpleLayoutPanel getLoginShell() {
		return loginShell;
	}

	@Override
	public void showLoginShell() {
		logo.setVisible(false);
		userBar.setVisible(false);
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(loginShell);
	}

	@Override
	public void showAppShell() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(this);
		RootPanel.get().add(logo);
		logo.setVisible(true);
		RootPanel.get().add(userBar);
		userBar.setVisible(true);
	}

	@Override
	public void constructBar(User object) {
		userBar.addStyleName("user-Bar");
		userBar.clear();
		Label user = new Label(object.getFirstName());
		user.addStyleName("user-Bar-Green");
		user.addStyleName("hand");
		user.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(new HomePlace()));
			}
		});
		userBar.add(user);
	Label sep  = new Label(" | ");
		userBar.add(sep);
		userBar.setCellWidth(sep, "20px");
		userBar.setCellHorizontalAlignment(sep, HasHorizontalAlignment.ALIGN_CENTER);
		Label logout = new Label("Logout");
		logout.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Injector.INSTANCE.getApplication().logout();
			}
		});
		logout.addStyleName("user-Bar-Green");
		logout.addStyleName("hand");
		userBar.add(logout);
//		RootPanel.get().add(userBar);
		userBar.setVisible(true);
	}
}
