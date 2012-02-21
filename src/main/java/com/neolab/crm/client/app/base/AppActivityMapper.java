package com.neolab.crm.client.app.base;

import java.util.logging.Logger;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.neolab.crm.client.mvp.activities.MembersActivity;
import com.neolab.crm.client.mvp.activities.DocumentsActivity;
import com.neolab.crm.client.mvp.activities.HomeActivity;
import com.neolab.crm.client.mvp.activities.LoginActivity;
import com.neolab.crm.client.mvp.activities.ProjectsActivity;
import com.neolab.crm.client.mvp.places.MembersPlace;
import com.neolab.crm.client.mvp.places.DocumentsPlace;
import com.neolab.crm.client.mvp.places.HomePlace;
import com.neolab.crm.client.mvp.places.LoginPlace;
import com.neolab.crm.client.mvp.places.ProjectsPlace;
import com.neolab.crm.client.utils.Util;

public class AppActivityMapper implements ActivityMapper{
	private static final Logger log = Logger.getLogger(Place.class.getName());

		
	public AppActivityMapper() {
	}

	@Override
	public Activity getActivity(Place place) {
		if(place instanceof HomePlace)
			return new HomeActivity();
		if(place instanceof ProjectsPlace){
			ProjectsPlace pp = (ProjectsPlace) place;
			return new ProjectsActivity(pp.getPid());
		}
		if(place instanceof DocumentsPlace)
			return new DocumentsActivity();
		if(place instanceof MembersPlace)
			return new MembersActivity();
		if(place instanceof LoginPlace){
//			return new HomeActivity();
			GWT.log("Ignoring login place");
			return new LoginActivity();
		}
//		else
			Util.logWarn(log, "Invalid Place: "+place);
		return null;
	}

}
