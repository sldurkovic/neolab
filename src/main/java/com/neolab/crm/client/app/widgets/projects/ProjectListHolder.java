package com.neolab.crm.client.app.widgets.projects;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.projects.CategoryExplorer.SelectedProjectHandler;
import com.neolab.crm.client.app.widgets.tables.ProjectsTable;
import com.neolab.crm.client.app.widgets.tables.TableHandler;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.rpc.ObjectResponse;
import com.neolab.crm.shared.resources.rpc.Response;

public class ProjectListHolder extends VerticalContainer{
	
	private int cid;
	
	private SelectedProjectHandler handler;
	
	public ProjectListHolder(int cid, SelectedProjectHandler handler) {
		super(false);
		this.cid = cid;
		this.handler = handler;
		render();
	}

	@Override
	protected void render() {
		VerticalPanel container = new VerticalPanel();
		container.setWidth("100%");
		
		//header
//		Sm label = new Label("Category projects");
//		label.addStyleName("category-Projects-Header");
//		container.add(label);
		
		PushButton newProject = new PushButton("New project");
		newProject.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new NewProjectDialog(cid, new NewProjectDialog.NewProjectHandler() {
					@Override
					public void createProject(String title, int cid, String description,  final Requires<Integer> caller) {
						Injector.INSTANCE.getNeoService().createProject(title, description, cid, new AbstractAsyncCallback<ObjectResponse<Integer>>() {
							public void success(ObjectResponse<Integer> result) {
								caller.delivery(result.getObject());
							};
						});
					}
				});
			}
		});
		container.add(new ProjectsTable(cid, new TableHandler<Project>() {
			
			@Override
			public void onRowClick(Project project) {
				handler.onSelect(project);
			}
		}));
		HTML glue = new HTML("&nbsp;");
		container.add(glue);
		container.setCellHeight(glue, "10px");
		container.add(newProject);
		container.setCellHorizontalAlignment(newProject, HasHorizontalAlignment.ALIGN_LEFT);
		addWidget(container);
	}
	
}