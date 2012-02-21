package com.neolab.crm.client.mvp.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.NeoPanel;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.mvp.BaseView;
import com.neolab.crm.client.mvp.activities.LoginActivity;

public class LoginView extends BaseView<LoginActivity> {

	private TextBox username;
	private TextBox password;

	public LoginView(LoginActivity p) {
		super(p);
		render();
	}

	@Override
	public void render() {
		username = new TextBox();
		username.setText("slobodan@gmail.com");
		password = new PasswordTextBox();
		password.setText("1234");
		password.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode() == 13) {
					presenter.login(username.getText(), password.getText());
				}
			}
		});
		Label uLabel = new Label(Injector.INSTANCE.getConstants().username()+": ");
		uLabel.addStyleName("login-Label");
		Label pLabel = new Label(Injector.INSTANCE.getConstants().password()+": ");
		pLabel.addStyleName("login-Label");
		PushButton login = new PushButton(Injector.INSTANCE.getConstants().login());
		login.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.login(username.getText(), password.getText());
			}
		});
		login.addStyleName("margin-Auto");
		FlexTable form = new FlexTable();
	    FlexCellFormatter cellFormatter = form.getFlexCellFormatter();
		form.addStyleName("login-Form");
		form.setWidget(0, 0, uLabel);
		form.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		form.setWidget(1, 0, pLabel);
		form.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
		form.setWidget(0, 1, username);
		form.setWidget(1, 1, password);
		cellFormatter.setColSpan(2, 0, 2);
		form.setWidget(2, 0, login);
//		Label google = new Label("Google");
//		google.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
////				presenter.loginViaGoogle();
//			}
//		});
//		form.setWidget(3, 0, google);

		NeoPanel panel = new NeoPanel();
		panel.addWidget(form);
		panel.setSize("280px","120px");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHeight(Window.getClientHeight()+"px");
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vp.addStyleName("margin-Auto");
		vp.add(panel);
		addWidget(vp);
		getTopContainer().setWidth("100%");
	}

	public void informUser(String msg) {
		Window.alert(msg);
	}

}
