package com.neolab.crm.client.app.widgets.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.tables.TableHandler;
import com.neolab.crm.client.app.widgets.tables.TasksTable;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.resources.rpc.Response;

public class AssignTaskDialog extends FormDialog implements HasAsyncInformation{
	
	private TasksTable table;
	private Project project;
	
	public AssignTaskDialog(Project project, final int uid) {
		this.project = project;
		
		Label head = new Label("Select tasks to assign:");
		head.addStyleName("formDialog-Title");
		addWidget(head);
		setGlassStyleName("glass-Second-Level");
		addStyleName("z-Index-10");
		table = new TasksTable(project.getPid(), uid, true, true, new TableHandler<Task>() {
			@Override
			public void onRowClick(Task object) {
			}
		});
		addWidget(table);
		addWidget(WidgetFactory.getBigButton("Ok", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(table.getSelectedSet() != null && table.getSelectedSet().size() > 0)
				Injector.INSTANCE.getNeoService().assignTasks(table.getSelectedSet(), uid, new AbstractAsyncCallback<Response>() {
					
					@Override
					public void success(Response result) {
						WidgetFactory.notify(result);
						hide();
					}
				});
				else
					hide();
				AssignTaskDialog.this.removeStyleName("z-Index-10");
			}
		}));

	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}

	@Override
	public void showInfo(Response response) {
		Window.alert("Task(s) assigned!");
		hide();
	}
}
	
