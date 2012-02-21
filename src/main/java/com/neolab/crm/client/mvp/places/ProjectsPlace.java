package com.neolab.crm.client.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.neolab.crm.shared.resources.HasLabel;

public class ProjectsPlace extends Place implements HasLabel{

	private static final String label = "projects";
	public static Integer tabID = 1;
	private int pid;
	
	public ProjectsPlace(int pid){
		this.pid = pid;
	}

	@Override
	public String getLabel() {
		return label;
	}
	

	public int getPid() {
		return pid;
	}

	public static class Tokenizer implements PlaceTokenizer<ProjectsPlace>
	{

		@Override
		public String getToken(ProjectsPlace place)
		{
			return label;
		}

		@Override
		public ProjectsPlace getPlace(String token)
		{
			return new ProjectsPlace(-1);
		}

	}


	@Override
	public String toString() {
		return label;
	}

	
}
