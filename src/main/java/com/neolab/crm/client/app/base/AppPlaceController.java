package com.neolab.crm.client.app.base;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.gin.Injector;

public class AppPlaceController extends PlaceController{
	
	@Inject
	public AppPlaceController(EventBus eventBus) {
		super(eventBus);
		eventBus.addHandler(GoToPlaceEvent.TYPE, new GoToPlaceEvent.Handler<GoToPlaceEvent>() {
			@Override
			public void on(GoToPlaceEvent e) {
				goTo(e.getPlaceToGo());
			}
		});
	}

}
