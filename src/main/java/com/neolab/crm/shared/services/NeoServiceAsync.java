package com.neolab.crm.shared.services;

import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

public interface NeoServiceAsync {

	void registerUser(String name, String email, int level, AsyncCallback<Response> callback);

	void requestUsersTable(int start, int limit, ColumnSort sort,
			AsyncCallback<TablePage<User>> abstractAsyncCallback);

	void login(String username, String password,
			AsyncCallback<UserResponse> callback);

	void updateUser(User user, AsyncCallback<Response> callback);


	void updateUsers(ArrayList<User> list,
			AsyncCallback<Response> abstractAsyncCallback);


	
	void logout(AsyncCallback<Response> callback);

	void getActiveUser(AsyncCallback<User> callback);

	void requestProjectsTable(int start, int length, ColumnSort sort, int cid,
			AsyncCallback<TablePage<Project>> asyncCallback);

	void requestTasksTable(int start, int length, ColumnSort sort, int pid,
			AsyncCallback<TablePage<Task>> asyncCallback);

	void getUserTasksTable(int start, int length, ColumnSort sort, int pid, int uid, boolean reverse,
			AsyncCallback<TablePage<Task>> asyncCallback);

	
	void sendMail(String to, int uidFrom, String subject, String body,
			AsyncCallback<Response> callback);

	void getUserCategoryProjects(int uid, int cat,
			AsyncCallback<ObjectResponse<ArrayList<Project>>> callback);

	void createProject(String title, String description, int cid,
			AsyncCallback<ObjectResponse<Integer>> abstractAsyncCallback);

	void getProjectById(int pid,
			AsyncCallback<ObjectResponse<ProjectPage>> abstractAsyncCallback);

	void requestUsersCellList(int pid, int start, int length,
			AsyncCallback<TablePage<User>> abstractAsyncCallback);

	void addActivity(int pid, ArrayList<Integer> uids,
			AsyncCallback<Response> callback);

	void removeActivity(int pid, ArrayList<Integer> uids,
			AsyncCallback<Response> callback);

	void addNewTask(int pid, String title, String description, Date start,
			Date end, AsyncCallback<Response> callback);

	void assignTask(int tid, ArrayList<Integer> uids,
			AsyncCallback<Response> callback);

	void assignTasks(ArrayList<Integer> tids, int uid,
			AsyncCallback<Response> callback);

	void updateProject(Project project, AsyncCallback<Response> callback);

	void deleteProject(int pid, AsyncCallback<Response> callback);

	void getUserProjects(int uid,
			AsyncCallback<ObjectResponse<ArrayList<Project>>> callback);

	void getConfiguration(
			AsyncCallback<Configuration> callback);

	void requestUsersTableByTask(int start, int length, ColumnSort sort,
			int tid,
			boolean reverse, AsyncCallback<TablePage<User>> callback);

	void requestTasksTableByDate(int start, int length, ColumnSort sort, int uid, int pid, Date date,
			AsyncCallback<TablePage<Task>> callback);
	
	void constructTimelineXml(int uid, AsyncCallback<String> callback);

	void getNews(int level, int uid, int start, int end,
			int pid, AsyncCallback<TablePageReponse<News>> callback);

	void createNews(int uid, String title, String body, int level, int pid,
			AsyncCallback<Response> callback);
	
	void changeStatus(int tid, TaskStatus status, AsyncCallback<Response> callback);

	void createCategory(String title, AsyncCallback<Response> callback);

	void getCategories(AsyncCallback<ArrayList<Category>> callback);

	
}
