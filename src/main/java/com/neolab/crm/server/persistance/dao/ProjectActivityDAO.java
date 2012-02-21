package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;

import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.ProjectActivity;

public interface ProjectActivityDAO {

	ArrayList<ProjectActivity> getUserProjectActivity(int uid);
	
	ArrayList<Integer> getUsersOnProject(int pid);
	
	int getUsersOnProjectCount(int pid);

	void addActivity(int pid, int uid);

	void removeActivity(int pid, ArrayList<Integer> uids);

	boolean isProjectMember(int uid, int pid);

}
