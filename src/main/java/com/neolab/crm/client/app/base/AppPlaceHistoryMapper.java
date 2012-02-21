package com.neolab.crm.client.app.base;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.neolab.crm.client.mvp.places.MembersPlace;
import com.neolab.crm.client.mvp.places.DocumentsPlace;
import com.neolab.crm.client.mvp.places.HomePlace;
import com.neolab.crm.client.mvp.places.LoginPlace;
import com.neolab.crm.client.mvp.places.ProjectsPlace;

public class AppPlaceHistoryMapper implements PlaceHistoryMapper{

	private String delimiter = "/";

	@Override
	public Place getPlace(String token) {

        String[] tokens = token.split(delimiter, 2); 

            if (tokens[0].equals("home"))
            	return new HomePlace();
            if (tokens[0].equals("projects"))
            	return new ProjectsPlace(-1);
//            if (tokens[0].equals("documents"))
//            	return new DocumentsPlace();
            if (tokens[0].equals("members"))
            	return new MembersPlace();
            if (tokens[0].equals("login"))
            	return new LoginPlace();
            return null;
	}

	@Override
	public String getToken(Place place) {
		if(place instanceof HomePlace)
			return "home";
		if(place instanceof ProjectsPlace)
			return "projects";
		if(place instanceof DocumentsPlace)
			return "documents";
		if(place instanceof MembersPlace)
			return "members";
		if(place instanceof LoginPlace)
			return "login";
		return "";
	}

	
}
