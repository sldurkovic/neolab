package com.neolab.crm.client.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.neolab.crm.shared.resources.HasLabel;

public class DocumentsPlace extends Place implements HasLabel{

	private static final String label = "documents";
	public static Integer tabID = 2;
	
	public DocumentsPlace(){
	}

	@Override
	public String getLabel() {
		return label;
	}
	

	public static class Tokenizer implements PlaceTokenizer<DocumentsPlace>
	{

		@Override
		public String getToken(DocumentsPlace place)
		{
			return label;
		}

		@Override
		public DocumentsPlace getPlace(String token)
		{
			return new DocumentsPlace();
		}

	}

	@Override
	public String toString() {
		return label;
	}

}
