package com.neolab.crm.client.mvp.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.events.ProjectActivityEvent;
import com.neolab.crm.client.app.events.SwitchTabEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.projects.UserCategoryProjects;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.mvp.BasePresenter;
import com.neolab.crm.client.mvp.places.LoginPlace;
import com.neolab.crm.client.mvp.view.ProjectsView;
import com.neolab.crm.shared.domain.Category;
import com.neolab.crm.shared.domain.News;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ProjectPage;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.TablePage;
import com.neolab.crm.shared.resources.rpc.ObjectResponse;
import com.neolab.crm.shared.resources.rpc.Response;
import com.neolab.crm.shared.resources.rpc.TablePageReponse;

public class ProjectsActivity extends BasePresenter<ProjectsView> {

	private static final Logger log = Logger.getLogger(ProjectsActivity.class
			.getName());

	private int pid;
	public ProjectsActivity(int pid) {
		this.pid = pid;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// Util.logFine(log, "Starting activity PROJECTS");
		User user = Injector.INSTANCE.getApplication().getActiveUser();
		if (user != null) {
			Injector.INSTANCE.getEventBus().fireEvent(new SwitchTabEvent(1));
			ProjectsView pv =  Injector.INSTANCE.getViewFactory().getProjectsView(this);

			if(pid != -1)
				pv.openProject(pid);
			setDisplay(pv);
			panel.setWidget(display);
		} else {
			// GWT.log("user="+user);
			Injector.INSTANCE.getEventBus().fireEvent(
					new GoToPlaceEvent(new LoginPlace()));
		}

	}

	public void getUserCategoryProjects(int cid,
			final Requires<UserCategoryProjects> caller) {
		int uid = Injector.INSTANCE.getApplication().getActiveUser().getUid();
		final UserCategoryProjects ucp = new UserCategoryProjects(cid);
		GWT.log("request: uid=" + uid + " cid=" + cid);

		Injector.INSTANCE
				.getNeoService()
				.getUserCategoryProjects(
						uid,
						cid,
						new AbstractAsyncCallback<ObjectResponse<ArrayList<Project>>>() {
							@Override
							public void success(
									ObjectResponse<ArrayList<Project>> result) {
								// GWT.log("response=" + result);
								if (result.getStatus()) {
									ucp.setProjects(result.getObject());
								} else {
									ucp.setProjects(new ArrayList<Project>());
								}
								caller.delivery(ucp);
							}
						});
	}

	public void getProjectById(int pid, final Requires<ProjectPage> caller) {
		Injector.INSTANCE.getNeoService().getProjectById(pid,
				new AbstractAsyncCallback<ObjectResponse<ProjectPage>>() {
					@Override
					public void success(ObjectResponse<ProjectPage> result) {
						// GWT.log("response=" + result);
						caller.delivery(result.getObject());
					}
				});
	}

	public void requestUsersCellList(int pid, final HasData<User> display) {
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int length = range.getLength();
		// GWT.log("Attempting user cell list");
		Injector.INSTANCE.getNeoService().requestUsersCellList(pid, start,
				length, new AbstractAsyncCallback<TablePage<User>>() {
					public void success(TablePage<User> result) {
						ArrayList<User> list = (ArrayList<User>) result
								.getList();
						display.setRowCount(result.getTotal());
						display.setRowData(start, list);
					}
				});
	}

	public void addActivity(final int pid, ArrayList<Integer> uids,
			final HasAsyncInformation caller) {
		Injector.INSTANCE.getNeoService().addActivity(pid, uids,
				new AbstractAsyncCallback<Response>() {
					public void success(Response result) {
						if(caller != null)
							caller.showInfo(result);
						else{
						WidgetFactory.alert("You have joined the project!");
						Injector.INSTANCE
						.getEventBus()
						.fireEvent(
								new ProjectActivityEvent(pid, true));						}
					};
				});

	}

	public void addNewTask(int pid, String title, String description,
			Date start, Date end, final HasAsyncInformation caller) {
		Injector.INSTANCE.getNeoService().addNewTask(pid, title, description, start, end, new AbstractAsyncCallback<Response>() {
			
			@Override
			public void success(Response result) {
				caller.showInfo(result);
			}
		});
	}

	public void updateProject(Project project,
			final HasAsyncInformation caller) {
		Injector.INSTANCE.getNeoService().updateProject(project, new AbstractAsyncCallback<Response>() {
			
			@Override
			public void success(Response result) {
				caller.showInfo(result);
			}
		});
	}
	

	public void addTaskActivity(int tid, ArrayList<Integer> uids,
			final HasAsyncInformation caller) {
		Injector.INSTANCE.getNeoService().assignTask(tid, uids, new AbstractAsyncCallback<Response>() {
			public void success(Response result) {
				caller.showInfo(result);
			};
		});
	}
	
	public void createNews(String title, String body, int level, int pid,
			final HasAsyncInformation caller) {
		Injector.INSTANCE.getNeoService().createNews(
				Injector.INSTANCE.getApplication().getActiveUser().getUid(),
				title, body, level, pid, new AbstractAsyncCallback<Response>() {
					@Override
					public void success(Response result) {
						caller.showInfo(result);
					}
				});
	}
	
	public void getFeed(int level, int pid, int start, int end,
			final Requires<TablePage<News>> caller) {
		Injector.INSTANCE.getNeoService().getNews(level, pid,  0, start, end,
				new AbstractAsyncCallback<TablePageReponse<News>>() {

					public void success(TablePageReponse<News> result) {
						log.fine(result + "");
						caller.delivery(result.getResult());
					};
				});
	}

	public void createCategory(String title, final HasAsyncInformation caller) {
		Injector.INSTANCE.getNeoService().createCategory(title,
				new AbstractAsyncCallback<Response>() {

					public void success(Response result) {
						caller.showInfo(result);
					};
				});
	}

	public void getCategories(final Requires<ArrayList<Category>> caller) {
		Injector.INSTANCE.getNeoService().getCategories(new AbstractAsyncCallback<ArrayList<Category>>() {
			@Override
			public void success(ArrayList<Category> result) {
				caller.delivery(result);
			}
		});
	}
	

}
