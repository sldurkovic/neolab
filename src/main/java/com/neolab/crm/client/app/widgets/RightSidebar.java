package com.neolab.crm.client.app.widgets;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.events.NewProjectEvent;
import com.neolab.crm.client.app.events.ProjectUserChangeEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.dialogs.AnnouncementDialog;
import com.neolab.crm.client.app.widgets.projects.NewProjectDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.client.mvp.activities.HomeActivity;
import com.neolab.crm.client.mvp.places.ProjectsPlace;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.Option;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.rpc.ObjectResponse;

public class RightSidebar extends VerticalContainer {
	
	private VerticalPanel projectsPanel;
	private HomeActivity presenter;

	
	public RightSidebar(HomeActivity presenter) {
		super(false);
		this.presenter = presenter;
		render();
	}

	@Override
	protected void render() {
		addStyleName("right-Sidebar");
		Injector.INSTANCE.getEventBus().addHandler(ProjectUserChangeEvent.TYPE, new ProjectUserChangeEvent.Handler<ProjectUserChangeEvent>() {
			@Override
			public void on(ProjectUserChangeEvent e) {
				refreshProjects();
		}});

		Injector.INSTANCE.getEventBus().addHandler(NewProjectEvent.TYPE,
				new NewProjectEvent.Handler<NewProjectEvent>() {
					@Override
					public void on(NewProjectEvent e) {
						refreshProjects();
					}
				});


		renderOptions();
	}
	
	private void renderOptions(){
		VerticalPanel panel = new VerticalPanel();
		
		getTopContainer().add(panel);
		getTopContainer().setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_LEFT);
		
		Label label = new Label("Actions");
		panel.add(label);
		label.addStyleName("right-Sidebar-Label");
		
		if(Injector.INSTANCE.getApplication().hasPrivilege(Option.WRITE_NEWS)){
			HorizontalPanel item = new HorizontalPanel();
			item.addStyleName("right-Sidebar-Item");
			WidgetFactory.addPanelClick(item, new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					new AnnouncementDialog(0, new AnnouncementDialog.CreateNewsProvider() {
						
						@Override
						public void createNews(String title, String body, int level, int pid,
								HasAsyncInformation caller) {
							presenter.createNews(title, body, level, pid, caller);
						}
					});
				}
			});
			Image image = ImageFactory.greenPlus();
			item.add(image);
			item.setCellWidth(image, "20px");
			item.add(new Label("New news"));
			panel.add(item);
		}
		
		if(Injector.INSTANCE.getApplication().hasPrivilege(Option.CREATE_PROJECT)){
			panel.add(getOptionItem("New project", new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					new NewProjectDialog(-1, new NewProjectDialog.NewProjectHandler() {
						
						@Override
						public void createProject(String title, int cid, String description,
								final Requires<Integer> caller) {
							presenter.createProject(title, cid, description, caller);
						}
					});
				}
			}));
		}
		
		Label projects = new Label("Projects");
		projects.addStyleName("margin-Top-30");
		panel.add(projects);
		projects.addStyleName("right-Sidebar-Label");
		projectsPanel = new VerticalPanel();
		projectsPanel.add(ImageFactory.smallLoading());
		panel.add(projectsPanel);
		refreshProjects();
		
	}
	
	private HorizontalPanel getOptionItem(String name, ClickHandler handler){
		HorizontalPanel item = new HorizontalPanel();
		item.addStyleName("right-Sidebar-Item");
		WidgetFactory.addPanelClick(item, handler);
		Image image = ImageFactory.greenPlus();
		item.add(image);
		item.setCellWidth(image, "20px");
		item.add(new Label(name));
		return item;
	}
	
	void refreshProjects(){
		Injector.INSTANCE
		.getNeoService()
		.getUserProjects(
				Injector.INSTANCE.getApplication().getActiveUser()
						.getUid(),
				new AbstractAsyncCallback<ObjectResponse<ArrayList<Project>>>() {

					@Override
					public void success(
							ObjectResponse<ArrayList<Project>> result) {
						projectsPanel.clear();
						for (Project p : result.getObject()) {
							projectsPanel.add(getProjectItem(p));
						}
					}
				});
	}
	

	
	private HorizontalPanel getProjectItem(final Project project){
		HorizontalPanel item = new HorizontalPanel();
		item.addStyleName("right-Sidebar-Item");
		WidgetFactory.addPanelClick(item, new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(new ProjectsPlace(project.getPid())));
			}
		});
		Image image = ImageFactory.projectSmallIco();
		item.add(image);
		item.setCellWidth(image, "20px");
		item.add(new Label(project.getTitle()));
		return item;
	}
	

}
