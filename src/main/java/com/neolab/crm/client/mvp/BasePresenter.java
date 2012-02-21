package com.neolab.crm.client.mvp;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.mvp.BaseMVP.Presenter;

public abstract class BasePresenter<D extends BaseView<?>> extends AbstractActivity implements Presenter<D> {
	
	protected D display;
	
	public BasePresenter(){
	}
	
	@Override
	public void goTo(Place place) {
		Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(place));
	}
	
	public void setDisplay(D display) {
		this.display = display;
	};
	
}
