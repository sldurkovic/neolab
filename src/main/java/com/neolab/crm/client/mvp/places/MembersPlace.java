package com.neolab.crm.client.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.neolab.crm.shared.resources.HasLabel;

public class MembersPlace extends Place implements HasLabel{

	private static final String label = "members";
	public static Integer tabID = 2;
	
	public MembersPlace(){
	}

	@Override
	public String getLabel() {
		return label;
	}
	

	public static class Tokenizer implements PlaceTokenizer<MembersPlace>
	{

		@Override
		public String getToken(MembersPlace place)
		{
			return label;
		}

		@Override
		public MembersPlace getPlace(String token)
		{
			return new MembersPlace();
		}

	}

	@Override
	public String toString() {
		return label;
	}

}
