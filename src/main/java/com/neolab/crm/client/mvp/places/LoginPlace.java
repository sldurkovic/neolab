package com.neolab.crm.client.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.neolab.crm.shared.resources.HasLabel;

public class LoginPlace extends Place implements HasLabel{

	private static final String label = "login";
	
	public LoginPlace(){
	}

	@Override
	public String getLabel() {
		return label;
	}
	

	public static class Tokenizer implements PlaceTokenizer<LoginPlace>
	{

		@Override
		public String getToken(LoginPlace place)
		{
			return label;
		}

		@Override
		public LoginPlace getPlace(String token)
		{
			return new LoginPlace();
		}

	}

	@Override
	public String toString() {
		return label;
	}

}
