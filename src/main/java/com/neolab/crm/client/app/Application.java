package com.neolab.crm.client.app;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.inject.Inject;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.base.AppActivityMapper;
import com.neolab.crm.client.app.base.AppPlaceHistoryMapper;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.events.ReloadActiveUserEvent;
import com.neolab.crm.client.app.events.UserDeliveryEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.mvp.activities.LoginActivity;
import com.neolab.crm.client.mvp.places.HomePlace;
import com.neolab.crm.client.mvp.places.LoginPlace;
import com.neolab.crm.client.mvp.places.MembersPlace;
import com.neolab.crm.client.mvp.places.ProjectsPlace;
import com.neolab.crm.client.mvp.view.ViewFactory;
import com.neolab.crm.client.utils.Util;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.Configuration;
import com.neolab.crm.shared.resources.Option;
import com.neolab.crm.shared.resources.rpc.Response;
import com.neolab.crm.shared.services.NeoServiceAsync;

public class Application {

	private static final Logger log = Logger.getLogger(Application.class.getName());
	
	public static final String ERROR_MESSAGE = "Error occured";
	
	public User activeUser;
	private ApplicationShell shell;
	private EventBus eventBus;
	private PlaceController placeController;
	private PlaceHistoryHandler placeHistoryHandler;
	private NeoServiceAsync service;
	private Configuration configuration;
	
	@Inject
	public Application(ApplicationShell shell, EventBus eventBus, PlaceController placeController, PlaceHistoryHandler placeHistoryHandler, NeoServiceAsync service) {
		this.shell = shell;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.placeHistoryHandler = placeHistoryHandler;
		this.service = service;
	}

	public void run(){
		/* Add handlers, setup activities */
		init();

		/* Hide the loading message */
		Element loading = Document.get().getElementById("loading");
		loading.getParentElement().removeChild(loading);

//		/* And show the user the shell */
//		RootLayoutPanel.get().add(shell.getDisplay());
//		RootLayoutPanel.get().getElement().getFirstChildElement().getNextSiblingElement().getStyle().clearOverflow();

	}

	private void init() {
//		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
//			public void onUncaughtException(Throwable e) {
//				Window.alert("Error: " + e.getMessage());
//				log.log(Level.SEVERE, e.getMessage(), e);
//			}
//		});
//		
//		service.login(new AbstractAsyncCallback<String>() {
//			
//			@Override
//			public void success(String result) {
//				log.fine("service passed: "+result);
//				
//			}
//		});


		
		ActivityManager masterActivityManager = new ActivityManager(new ActivityMapper() {
			
			@Override
			public Activity getActivity(Place place) {
				if(place instanceof LoginPlace)
					return new LoginActivity();
				return null;
			}
		}, eventBus);
		
		masterActivityManager.setDisplay(shell.getLoginShell());
		
		Util.logFine(log, "Initializing activites and places");
		AppActivityMapper mapper = new AppActivityMapper();
		ActivityManager activityManager = new ActivityManager(mapper, eventBus);
		activityManager.setDisplay(shell.getNeoTabPanel().getContentWidget());
		
        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
        final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, new LoginPlace());
		
		Injector.INSTANCE.getNeoService().getConfiguration(new AbstractAsyncCallback<Configuration>() {
			@Override
			public void success(Configuration result) {
				configuration = result;
				 Injector.INSTANCE.getNeoService().getActiveUser(new AbstractAsyncCallback<User>() {
			        	@Override
			        	public void success(User result) {
			        		if(result != null){
			    				setActiveUser(result);
			    				GWT.log("active_user="+result);
					    		Injector.INSTANCE.getApplicationShell().showAppShell();
			    			}else{
			    				Injector.INSTANCE.getApplicationShell().showLoginShell();
			    			}
					        historyHandler.handleCurrentHistory();
			        	}
					});

			}
		});
        
       
        
        Injector.INSTANCE.getEventBus().addHandler(ReloadActiveUserEvent.TYPE, new ReloadActiveUserEvent.Handler<ReloadActiveUserEvent>(){
			@Override
			public void on(ReloadActiveUserEvent e) {
				Injector.INSTANCE.getNeoService().getActiveUser(new AbstractAsyncCallback<User>() {
		        	@Override
		        	public void success(User result) {
		        		if(result == null){
		        			Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(new LoginPlace()));
		        		}else{
		    				activeUser = result;
		    				Injector.INSTANCE.getEventBus().fireEvent(new UserDeliveryEvent(result));
		    			}
		        	}
				});
			}
		});
//        Injector.INSTANCE.getEventBus().fireEvent(new ReloadActiveUserEvent());

        
	}

	public Place getPlaceById(int placeToGo) {
		switch (placeToGo) {
		case 0:
			return new HomePlace();
		case 1:
			return new ProjectsPlace(-1);
		case 2:
//			return new DocumentsPlace();
//		case 3:
			return new MembersPlace();
		default:
			return new HomePlace();
		}
	}

	public User getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(User object) {
		if(object != null){
			shell.constructBar(object);
		}
		activeUser = object;
	}
	
//	@Inject
//	public void showShell(){
//		RootLayoutPanel.get().clear();
//		RootLayoutPanel.get().add(shell.getDisplay());
//	}

	public void logout() {
		Injector.INSTANCE.getNeoService().logout(new AbstractAsyncCallback<Response>() {
			public void success(Response result) {
//				Cookies.removeCookie("JSESSIONID");
				Injector.INSTANCE.getApplication().setActiveUser(null);
				Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(new LoginPlace()));
				Injector.INSTANCE.getViewFactory().clear();
			};
		});
	}
	
	public boolean hasPrivilege(Option option){
		return configuration.getPrivileges().authorize(activeUser.getLevel(), option);
	}
	public String getLevelName(int level){
		return configuration.getPrivileges().getName(level);
	}
	
	public int getLevelNumber(String level){
		return configuration.getPrivileges().getLevel(level);
	}
	
	public ArrayList<String> getLevels(){
		return configuration.getPrivileges().getStringLevels();
	}
}
