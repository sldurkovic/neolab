package com.neolab.crm.server.persistance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neolab.crm.server.persistance.dao.CategoryDAO;
import com.neolab.crm.server.persistance.dao.NewsDAO;
import com.neolab.crm.server.persistance.dao.ProjectActivityDAO;
import com.neolab.crm.server.persistance.dao.ProjectsDAO;
import com.neolab.crm.server.persistance.dao.TaskActivityDAO;
import com.neolab.crm.server.persistance.dao.TasksDAO;
import com.neolab.crm.server.persistance.dao.UsersDAO;
import com.neolab.crm.shared.domain.Category;
import com.neolab.crm.shared.domain.News;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.ProjectActivity;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.TaskStatus;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

@Transactional
@Service
public class Database {

	@Autowired
	private UsersDAO usersDao;

	@Autowired
	private ProjectsDAO projectsDao;

	@Autowired
	private TasksDAO tasksDao;

	@Autowired
	private ProjectActivityDAO paDao;

	@Autowired
	private TaskActivityDAO taDao;

	@Autowired
	private CategoryDAO catDao;
	
	@Autowired
	private NewsDAO newsDao;

	public ArrayList<User> getUsers(int offset, int limit, ColumnSort sort) {
		return usersDao.getUsers(offset, limit, sort);
	}

	public ArrayList<User> getUsersOnProject(int pid, int offset, int limit,
			ColumnSort sort) {
		return usersDao.getUsersById(paDao.getUsersOnProject(pid), offset,
				limit, sort);
	}

	public int getUsersCount() {
		return usersDao.getUsersCount();
	}

	public int getUsersOnProjectCount(int pid) {
		return paDao.getUsersOnProjectCount(pid);
	}

	public boolean registerUser(String name, String email, int level,  String password) {
		return usersDao.registerUser(name, email, level, password);
	}

	public User login(String username, String password) {
		return usersDao.login(username, password);
	}

	public boolean updateUser(User user) {
		return usersDao.update(user);
	}

	public int getProjectsCount(int cid) {
		return projectsDao.getProjectCount(cid);
	}

	public ArrayList<Project> getProjects(int offset, int limit,
			ColumnSort sort, int cid) {
		return projectsDao.getProjects(offset, limit, sort, cid);
	}

	public int getTasksCount(int pid) {
		return tasksDao.getTaskCount(pid);
	}

	public ArrayList<Task> getTasks(int start, int length, ColumnSort sort,
			int pid) {
		return tasksDao.getTasks(start, length, sort, pid);
	}

	public int getUserTasksCount(int pid, int uid, boolean reverse) {
		return tasksDao.getUserTaskCount(pid, uid, reverse);
	}

	public ArrayList<Task> getUserTasks(int start, int length, ColumnSort sort,
			int pid, int uid, boolean reverse) {
		return tasksDao.getUserTasks(start, length, sort, pid, uid, reverse);
	}

	public User getUser(int uid) {
		return usersDao.getUser(uid);
	}

	public ArrayList<Project> getUserCategoryProjects(int uid, int cat) {
		ArrayList<ProjectActivity> result = paDao.getUserProjectActivity(uid);
		if(result == null)
			return new ArrayList<Project>();
		List<Project> list = projectsDao.getProjectsByActivity(result, cat);
		return (ArrayList<Project>) list;
	}

	public int createProject(int uid, String title, String description, int cid) {
		int pid = projectsDao.createProject(uid, title, description, cid);
		ArrayList<Integer> uids = new ArrayList<Integer>();
		uids.add(uid);
		addActivity(pid, uids);
		return pid;
	}

	public void addActivity(int pid, ArrayList<Integer> uids) {
		if (uids != null)
			for (Integer uid : uids) {
				paDao.addActivity(pid, uid);
			}
	}

	public Project getProjectById(int pid) {
		return projectsDao.getProject(pid);
	}

	public void removeActivity(int pid, ArrayList<Integer> uids) {
		paDao.removeActivity(pid, uids);
		taDao.removeTasksActivity(tasksDao.getTasksForProject(pid), uids);
	}

	public void addNewTask(int pid, String title, String description,
			Date start, Date end) {
		tasksDao.addTask(pid, title, description, start, end);
	}

	public void deleteTask(int i) {
		tasksDao.deleteTask(i);
	}

	public void assignTask(int tid, ArrayList<Integer> uids) {
		taDao.assignTask(tid, uids);
	}

	public void assignTasks(ArrayList<Integer> tids, int uid) {
		taDao.assignTasks(tids, uid);
	}

	public void removeAssignedTasks(ArrayList<Integer> tids, int uid) {
		taDao.removeAssignedTasks(tids, uid);
	}

	public void changeStatus(int tid, TaskStatus status) {
		tasksDao.changeStatus(tid, status);
	}

	public void updateProject(Project project) {
		projectsDao.updateProject(project);
	}

	public void deleteProject(int pid) {
		projectsDao.deleteProject(pid);
	}

	public ArrayList<Project> getUserProjects(int uid) {
		ArrayList<ProjectActivity> result = paDao.getUserProjectActivity(uid);
		if(result.size() == 0)
			return new ArrayList<Project>();
		List<Project> list = projectsDao.getProjectsByActivity(result, -1);
		return (ArrayList<Project>) list;
	}

	public int getUsersCountForTask(int tid, boolean reverse) {
		return taDao.getUsersCountForTask(tid, reverse);
	}

	public ArrayList<User> getUsersForTask(int start, int length,
			ColumnSort sort, int tid, boolean reverse) {
		return taDao.getUsersForTask(start, length, sort, tid, reverse);
	}

	public int getTasksCountByDate(int uid, int pid, Date date) {
		if(uid == 0)
			return tasksDao.getTaskCountByDate(pid, date);
		return taDao.getTaskCountByDate(uid, pid, date);
	}

	public ArrayList<Task> getTasksByDate(int start, int length,
			ColumnSort sort, int uid, int pid, Date date) {
		if(uid == 0)
			return tasksDao.getTasksByDate(start, length, sort, uid, pid, date);
		return taDao.getTasksByDate(start, length, sort, uid, pid, date);
	}
	
	public ArrayList<Task> getTasksByDate(int pid, Date date) {
		return tasksDao.getTasksByDate(pid, date);
	}

	public ArrayList<News> getNews(int level, ArrayList<Integer> pids, int start, int end) {
		return newsDao.getNews(level, pids, start, end);
	}

	public int getNewsCount(int level, ArrayList<Integer> pids) {
		return newsDao.getNewsCount(level, pids);
	}
	

	public ArrayList<News> getNewsForProject(int level, int pid, int start, int end) {
		return newsDao.getNewsForProject(level, pid, start, end);
	}

	public int getNewsForProjectCount(int level, int pid) {
		return newsDao.getNewsForProjectCount(level, pid);
	}
	
	public void createNews(int uid, String title, String body, int level, int pid ){
		newsDao.createNews(uid, title, body, level, pid);
	}

	public boolean isProjectMember(int uid, int pid) {
		return paDao.isProjectMember(uid, pid);
	}

	public void createCategory(String title) {
		catDao.createCategory(title);
	}

	public ArrayList<Category> getCategories() {
		return catDao.getCategories();
	}

	public Task getTaskById(int tid) {
		return tasksDao.getTaskById(tid);
	}


}
