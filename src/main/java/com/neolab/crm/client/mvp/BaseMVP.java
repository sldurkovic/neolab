package com.neolab.crm.client.mvp;

import java.util.logging.Logger;

import com.google.gwt.place.shared.Place;

public class BaseMVP {

	protected Logger log = Logger.getLogger(getClass().getName());
	
	public interface Presenter<D extends View<?>>{
		void setDisplay(D display);
	    void goTo(Place place);
	}
	
	public interface View<P extends Presenter<?>>{
		void setPresenter(P presenter);
//		void render();
	}

}
