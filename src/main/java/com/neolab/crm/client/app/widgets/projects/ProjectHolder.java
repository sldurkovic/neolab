package com.neolab.crm.client.app.widgets.projects;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.ProjectActivityEvent;
import com.neolab.crm.client.app.events.ProjectUpdatedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.ButtonDeck;
import com.neolab.crm.client.app.widgets.CalendarWidget;
import com.neolab.crm.client.app.widgets.NewsStream;
import com.neolab.crm.client.app.widgets.PropertyTable;
import com.neolab.crm.client.app.widgets.StackSideDeck;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.dialogs.AddUserDialog;
import com.neolab.crm.client.app.widgets.dialogs.AnnouncementDialog;
import com.neolab.crm.client.app.widgets.dialogs.EditProjectDialog;
import com.neolab.crm.client.app.widgets.dialogs.NewTaskDialog;
import com.neolab.crm.client.app.widgets.dialogs.TaskPreviewDialog;
import com.neolab.crm.client.app.widgets.dialogs.UploadFileDialog;
import com.neolab.crm.client.app.widgets.tables.TableHandler;
import com.neolab.crm.client.app.widgets.tables.TasksTable;
import com.neolab.crm.client.app.widgets.tables.UsersCellList;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.fwk.containers.HorizontalContainer;
import com.neolab.crm.client.mvp.activities.DocumentsActivity;
import com.neolab.crm.client.mvp.activities.ProjectsActivity;
import com.neolab.crm.shared.domain.News;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.Option;
import com.neolab.crm.shared.resources.ProjectPage;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.TablePage;
import com.neolab.crm.shared.resources.rpc.TablePageReponse;

public class ProjectHolder extends HorizontalContainer implements
		Requires<ProjectPage> {

	private Project project;
	private ProjectPage page;
	private TasksTable tasksTable;
	private ProjectsActivity presenter;

	public ProjectHolder(final Project project, final ProjectsActivity presenter) {
		super(false);
		this.presenter = presenter;
		this.project = project;

		// gets project page
		presenter.getProjectById(project.getPid(), this);

		Injector.INSTANCE.getEventBus().addHandler(ProjectUpdatedEvent.TYPE,
				new ProjectUpdatedEvent.Handler<ProjectUpdatedEvent>() {
					@Override
					public void on(ProjectUpdatedEvent e) {
						if (e.getPid() == project.getPid()) {
							presenter.getProjectById(project.getPid(),
									ProjectHolder.this);
						}
					}
				});

		Injector.INSTANCE.getEventBus().addHandler(ProjectActivityEvent.TYPE,
				new ProjectActivityEvent.Handler<ProjectActivityEvent>() {
					@Override
					public void on(ProjectActivityEvent e) {
						if (e.getPid() == project.getPid()) {
							presenter.getProjectById(project.getPid(),
									ProjectHolder.this);
						}
					}
				});
	}

	@Override
	protected void render() {
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.setStyleName("project-Central");
		final Project p = page.getProject();
		PropertyTable des = new PropertyTable();
		des.setStyleName("project-Description");
		des.setWidth("100%");

		PushButton edit = new PushButton("Edit", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new EditProjectDialog(p, presenter);
			}
		});
		HorizontalPanel ptitle = new HorizontalPanel();
		ptitle.addStyleName("no-Padding");
		Label label1 = new Label(p.getTitle());
		ptitle.add(label1);
		ptitle.setCellVerticalAlignment(label1,
				HasVerticalAlignment.ALIGN_MIDDLE);
		WidgetFactory.glue(ptitle, "20px", "");
		if (Injector.INSTANCE.getApplication()
				.hasPrivilege(Option.EDIT_PROJECT)) {
			ptitle.add(edit);
		}
		des.insertWidget("Project title", ptitle);
		// des.insertWidget("Date created",
		// CfgConstants.DATE_FORMAT.format(p.getDateCreated()));
		if (!p.getDescription().isEmpty())
			des.insertWidget("Description", p.getDescription());

		// DOCUMENTS
		// if (page.getDocuments().size() > 0) {
		// DisclosurePanel docs = new DisclosurePanel("Documents");
		VerticalPanel docsPanel = new VerticalPanel();
		docsPanel.addStyleName("documents");
		for (String s : page.getDocuments()) {
			HorizontalPanel down = new HorizontalPanel();
			down.addStyleName("hand");
			final Label label = new Label(s);
			down.sinkEvents(Event.ONCLICK);
			final String doc = s;
			down.addHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Window.Location.replace("/neolab/download?pid="
							+ project.getPid() + "&document=" + URL.encode(doc));
				}
			}, ClickEvent.getType());
			Image docImage = ImageFactory.document();
			down.add(docImage);
			down.setCellVerticalAlignment(docImage,
					HasVerticalAlignment.ALIGN_MIDDLE);
			down.add(new HTML("&nbsp;"));
			down.add(label);
			down.add(new HTML("&nbsp;"));
			down.add(ImageFactory.download());
			docsPanel.add(down);
		}
			// docsPanel.add(uploadFile);
			des.insertWidget("Documents", docsPanel);

		PushButton uploadFile = new PushButton("Attach document",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						new UploadFileDialog(project);
					}
				});
		if (Injector.INSTANCE.getApplication().hasPrivilege(
				Option.UPLOAD_DOCUMENT)) {
			if(page.getDocuments().size()>0)
			docsPanel.add(new HTML("&nbsp;"));
			docsPanel.add(uploadFile);
		}

		PushButton delete = new PushButton("Delete project",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.alert("Delete project");
					}
				});

		PushButton news = new PushButton("Create news", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new AnnouncementDialog(project.getPid(),
						new AnnouncementDialog.CreateNewsProvider() {

							@Override
							public void createNews(String title, String body,
									int level, int pid,
									HasAsyncInformation caller) {
								presenter.createNews(title, body, level, pid,
										caller);
							}
						});
			}
		});

		panel.add(des);

		// buttons
		ButtonDeck bdeck = new ButtonDeck();
		if (page.isMember())
			panel.add(bdeck);
		bdeck.setTasksPanel(initTasksPanel());
		VerticalPanel n = new VerticalPanel();
		n.setWidth("100%");
		if (Injector.INSTANCE.getApplication().hasPrivilege(
				Option.WRITE_PROJECT_NEWS)) {
			n.add(news);
			WidgetFactory.glue(n, "10px");
		}
		n.add(new NewsStream(new NewsStream.FeedProvider() {

			@Override
			public void getFeed(int level, int start, int end,
					Requires<TablePage<News>> caller) {
				presenter.getFeed(level, project.getPid(), start, end, caller);
			}
		}));
		bdeck.setNewsPanel(n);
		bdeck.setCalendarPanel(new CalendarWidget(project.getPid(), true));

		addWidget(panel);

		getTopContainer().setCellWidth(panel, "100%");

		UsersCellList list = new UsersCellList(project, page.isMember(),
				new AbstractDataProvider<User>() {
					@Override
					protected void onRangeChanged(final HasData<User> display) {
						presenter.requestUsersCellList(project.getPid(),
								display);
					}
				}, new AddUserDialog.AddActivityHandler() {

					@Override
					public void addActivity(int pid, ArrayList<Integer> uids,
							HasAsyncInformation caller) {
						presenter.addActivity(pid, uids, caller);
					}
				});
		list.addStyleName("projects-UsersCellList");

		addWidget(list);
	}

	private Widget initTasksPanel() {
		// DisclosurePanel tasks = new DisclosurePanel("Tasks");
		VerticalPanel tasksPanel = new VerticalPanel();
		PushButton assignTask = new PushButton("Create new task",
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						new NewTaskDialog(project,
								new NewTaskDialog.NewTaskHandler() {

									@Override
									public void addNewTask(int pid,
											String title, String description,
											Date start, Date end,
											HasAsyncInformation caller) {
										presenter
												.addNewTask(pid, title,
														description, start,
														end, caller);
									}
								});
					}
				});
		tasksTable = new TasksTable(project.getPid(), -1, false, false,
				new TableHandler<Task>() {
					public void onRowClick(Task object) {
						new TaskPreviewDialog(project, object, presenter);
					};
				});
		tasksTable.setWidth("100%");
		tasksPanel.add(tasksTable);
		tasksPanel.setCellWidth(tasksTable, "100%");
		if (Injector.INSTANCE.getApplication().hasPrivilege(Option.ADD_TASK)) {
			tasksPanel.add(assignTask);
		}
		tasksPanel.setWidth("100%");

		// tasks.add(tasksPanel);
		// tasks.setOpen(true);
		return tasksPanel;
	}

	@Override
	public void delivery(ProjectPage object) {
		page = object;
		clear();
		render();
	}

}