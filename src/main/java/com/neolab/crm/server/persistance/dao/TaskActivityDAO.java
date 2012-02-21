package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import java.util.Date;

import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.TaskStatus;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

public interface TaskActivityDAO {
	
	void assignTask(int tid, ArrayList<Integer> uids);
	void removeAssignedTasks(ArrayList<Integer> tids, int uid);
	void assignTasks(ArrayList<Integer> tids, int uid);
	int getUsersCountForTask(int tid, boolean reverse);
	ArrayList<User> getUsersForTask(int start, int length, ColumnSort sort,
			int tid, boolean reverse);
	void removeTasksActivity(ArrayList<Task> arrayList, ArrayList<Integer> uids);
	int getTaskCountByDate(int uid, int pid, Date date);
	ArrayList<Task> getTasksByDate(int start, int length, ColumnSort sort,
			int uid, int pid, Date date);
}
