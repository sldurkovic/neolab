package com.neolab.crm.client.mvp.view;

import com.neolab.crm.client.mvp.activities.MembersActivity;
import com.neolab.crm.client.mvp.activities.HomeActivity;
import com.neolab.crm.client.mvp.activities.ProjectsActivity;

public class ViewFactory {
	
	private ProjectsView projectsView;
	private HomeView homeView;
	private MembersView membersView;
	
	public ViewFactory(){
	}
	
	public ProjectsView getProjectsView(ProjectsActivity presenter){
		if(projectsView == null)
			projectsView = new ProjectsView(presenter);
		return projectsView;
	}

	public HomeView getHomeView(HomeActivity presenter) {
		if(homeView == null)
			homeView = new HomeView(presenter);
		return homeView;
	}

	public MembersView getAdminView(MembersActivity presenter) {
		if(membersView == null)
			membersView = new MembersView(presenter);
		return membersView;
	}
	
	public void clear(){
		projectsView = null;
		homeView = null;
		membersView = null;
	}

}
