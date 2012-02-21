package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import java.util.List;

import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.ProjectActivity;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

public interface ProjectsDAO {

	int getProjectCount(int cid);

	ArrayList<Project> getProjects(int offset, int limit, ColumnSort sort, int cid);

	ArrayList<Project> getProjectsByActivity(ArrayList<ProjectActivity> result, int cat);

	int createProject(int uid, String title, String description, int cid);
	
	Project getProject(int pid);

	void updateProject(Project project);

	void deleteProject(int pid);

}
