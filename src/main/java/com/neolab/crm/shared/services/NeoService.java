package com.neolab.crm.shared.services;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.shared.domain.Category;
import com.neolab.crm.shared.domain.News;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.TaskStatus;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;
import com.neolab.crm.shared.resources.Configuration;
import com.neolab.crm.shared.resources.ProjectPage;
import com.neolab.crm.shared.resources.TablePage;
import com.neolab.crm.shared.resources.rpc.ObjectResponse;
import com.neolab.crm.shared.resources.rpc.Response;
import com.neolab.crm.shared.resources.rpc.TablePageReponse;
import com.neolab.crm.shared.resources.rpc.UserResponse;

@RemoteServiceRelativePath("neo.rpc")
public interface NeoService extends RemoteService{
	
	Configuration getConfiguration();

	UserResponse login(String username, String password);
	Response registerUser(String name, String email, int level);
	Response updateUser(User user);
	Response logout();
	TablePage<User> requestUsersTable(int start, int limit, ColumnSort sort);
	User getActiveUser();
	TablePage<Project> requestProjectsTable(int start, int length,ColumnSort sort, int cid);
	TablePage<Task> requestTasksTable(int start, int length, ColumnSort sort, int pid);
	TablePage<Task> getUserTasksTable(int start, int length, ColumnSort sort, int pid, int uid, boolean reverse);
	TablePage<User> requestUsersTableByTask(int start, int length, ColumnSort sort, int tid, boolean reverse);
	TablePage<Task> requestTasksTableByDate(int start, int length, ColumnSort sort, int uid, int pid, Date date);
	Response sendMail(String to, int uidFrom, String subject, String body);
	ObjectResponse<ArrayList<Project>> getUserCategoryProjects(int uid, int cat);
	ObjectResponse<Integer> createProject(String title, String description, int cid);
	ObjectResponse<ProjectPage> getProjectById(int pid);
	TablePage<User> requestUsersCellList(int pid, int start, int length);
	Response addActivity(int pid, ArrayList<Integer> uids);
	Response removeActivity(int pid, ArrayList<Integer> uids);
	Response addNewTask(int pid, String title, String description, Date start, Date end);
	Response assignTask(int tid, ArrayList<Integer> uids);
	Response assignTasks(ArrayList<Integer> tids, int uid);
	Response updateProject(Project project);
	Response deleteProject(int pid);
	ObjectResponse<ArrayList<Project>> getUserProjects(int uid);
	String constructTimelineXml(int uid);
	TablePageReponse<News> getNews(int level, int pid, int uid, int start, int end);
	Response createNews(int uid, String title, String body, int level, int pid);
	Response updateUsers(ArrayList<User> list);
	Response changeStatus(int tid, TaskStatus status);
	Response createCategory(String title);

	ArrayList<Category> getCategories();
}
