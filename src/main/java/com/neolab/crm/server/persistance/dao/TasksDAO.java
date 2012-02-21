package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import java.util.Date;

import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.TaskStatus;
import com.neolab.crm.shared.resources.ColumnSort;

public interface TasksDAO {

	int getTaskCount(int pid);

	ArrayList<Task> getTasks(int start, int length, ColumnSort sort, int pid);

	ArrayList<Task> getTasksForProject(int pid);
	
	void addTask(int pid, String title, String description, Date start, Date end);

	void deleteTask(int tid);
	
	void changeStatus(int tid, TaskStatus status);

	int getUserTaskCount(int pid, int uid, boolean reverse);

	ArrayList<Task> getUserTasks(int start, int length, ColumnSort sort,
			int pid, int uid, boolean reverse);


	ArrayList<Task> getTasksByDate(int start, int length, ColumnSort sort,
			int uid, int pid, Date date);

	int getTaskCountByDate(int pid, Date date);

	ArrayList<Task> getTasksByDate(int pid, Date date);

	Task getTaskById(int tid);
	
	
}
