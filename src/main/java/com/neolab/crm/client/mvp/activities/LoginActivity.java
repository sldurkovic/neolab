package com.neolab.crm.client.mvp.activities;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.events.ReloadActiveUserEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.mvp.BasePresenter;
import com.neolab.crm.client.mvp.places.HomePlace;
import com.neolab.crm.client.mvp.view.LoginView;
import com.neolab.crm.client.utils.Util;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.rpc.Response;
import com.neolab.crm.shared.resources.rpc.UserResponse;

public class LoginActivity extends BasePresenter<LoginView>{
	
	private static final Logger log = Logger.getLogger(LoginActivity.class.getName());
	public LoginActivity() {
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
//		Util.logFine(log, "Starting activity SECURITY");
//		  String sessionID = Cookies.getCookie("JSESSIONID");
		 User user = Injector.INSTANCE.getApplication().getActiveUser();
		    if ( user != null ){
//				Injector.INSTANCE.getApplication().showShell();
//	    		Injector.INSTANCE.getApplicationShell().showAppShell();
//		    	Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(new HomePlace()));
//		        Injector.INSTANCE.getEventBus().fireEvent(new ReloadActiveUserEvent());
//				Injector.INSTANCE.getApplicationShell().showApp();
//		    	if(Injector.INSTANCE.getApplicationShell().getLoginShell().isVisible())
//		    		Injector.INSTANCE.getApplicationShell().showAppShell();
				/* And show the user the shell */
//				Injector.INSTANCE.getApplication().showShell();
//				RootLayoutPanel.get().getElement().getFirstChildElement().getNextSiblingElement().getStyle().clearOverflow();
		    } else{
				Injector.INSTANCE.getApplicationShell().showLoginShell();
		    	setDisplay(new LoginView(this));	
				panel.setWidget(display);	
		    }
	}

	public void login(String username, String password) {
		Injector.INSTANCE.getNeoService().login(username, password, new AbstractAsyncCallback<UserResponse>() {
			@Override
			public void success(UserResponse result) {
				if(!result.getStatus()){
					display.informUser(result.getMsg());
				}else{
//					Injector.INSTANCE.getApplication().showShell();
					Injector.INSTANCE.getApplication().setActiveUser(result.getUser());
		    		Injector.INSTANCE.getApplicationShell().showAppShell();
					Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(new HomePlace()));
//					Cookies.setCookie("sid", result.getMsg(), null, null, "/", false);
				}
			}
		});
	}

}
