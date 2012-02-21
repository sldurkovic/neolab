package com.neolab.crm.client.app.widgets.dialogs;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.events.ProjectUpdatedEvent;
import com.neolab.crm.client.app.events.TaskUpdatedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.PropertyTable;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.projects.ProjectHolder;
import com.neolab.crm.client.app.widgets.tables.TaskUsersTable;
import com.neolab.crm.client.fwk.CompositeToggleButton;
import com.neolab.crm.client.fwk.ConfirmDialog;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.mvp.activities.ProjectsActivity;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.TaskStatus;
import com.neolab.crm.shared.resources.CfgConstants;
import com.neolab.crm.shared.resources.rpc.Response;

public class TaskPreviewDialog extends FormDialog implements HasAsyncInformation{
	
	private Task task;
	private DeckPanel deck;
	private Project project;
	private ProjectsActivity presenter;
	private SimplePanel statusPanel;
	
	public TaskPreviewDialog(Project project, Task task, ProjectsActivity presenter) {
		this.presenter = presenter;
		this.task = task;
		statusPanel = new SimplePanel();
		deck = new DeckPanel();
		deck.setWidth("100%");
		this.project = project;
		renderPanel();
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
		show();
		Injector.INSTANCE.getEventBus().addHandler(TaskUpdatedEvent.TYPE, new TaskUpdatedEvent.Handler<TaskUpdatedEvent>() {
			@Override
			public void on(TaskUpdatedEvent e) {
		}});
	}
	
	public TaskPreviewDialog(Task task) {
		this(null, task,null);
//		this.task = task;
//		deck = new DeckPanel();
//		deck.setWidth("100%");
//		renderPanel();
//	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
//		setPopupPosition(left, 70);
//		show();
	}


	private void renderPanel() {
		
		Label head = new Label("Task details");
		head.addStyleName("formDialog-Title");
		addWidget(head);
		
		HorizontalPanel bar = new HorizontalPanel();
		bar.setWidth("100%");
		final CompositeToggleButton ctb  = new CompositeToggleButton("FINISHED", "ACTIVE", true);
		ctb.setValue(TaskStatus.valueOf(task.getStatus()).getBoolean());
		ctb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(final ValueChangeEvent<Boolean> event) {
				if(TaskStatus.getStatus(event.getValue()) != TaskStatus.valueOf(task.getStatus())){
					Injector.INSTANCE.getNeoService().changeStatus(task.getTid(), TaskStatus.getStatus(event.getValue()), new AbstractAsyncCallback<Response>(){
						@Override
						public void success(Response result) {
							Injector.INSTANCE.getEventBus().fireEvent(new TaskUpdatedEvent());
						}
					});
					statusPanel.clear();
					statusPanel.setWidget(WidgetFactory.getStatusLabel(TaskStatus.getStatus(event.getValue()).toString()));
				}
			}
		});
		bar.add(ctb);
		bar.setCellHorizontalAlignment(ctb, HasHorizontalAlignment.ALIGN_LEFT);
		
		PushButton assign = new PushButton("Assign users", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new AddUsersToTaskDialog(task, new AddUsersToTaskDialog.AddTaskActivityHandler() {
					
					@Override
					public void addTaskActivity(int tid, ArrayList<Integer> uids,
							final HasAsyncInformation caller) {
						if(presenter != null)
						presenter.addTaskActivity(tid, uids, caller);
						else{
							Injector.INSTANCE.getNeoService().assignTask(tid, uids, new AbstractAsyncCallback<Response>() {
								public void success(Response result) {
									caller.showInfo(result);
								};
							});
						}
					}
				});
			}
		});
		
		PushButton edit = new PushButton("Edit", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("implement");
			}
		});

		HorizontalPanel right = new HorizontalPanel();
//		right.add(edit);
		right.setCellHorizontalAlignment(edit, HasHorizontalAlignment.ALIGN_RIGHT);
		WidgetFactory.glue(right);
		right.add(assign);
		right.setCellHorizontalAlignment(assign, HasHorizontalAlignment.ALIGN_RIGHT);
		bar.add(right);
		bar.setCellHorizontalAlignment(right, HasHorizontalAlignment.ALIGN_RIGHT);
		addWidget(bar);
		getContent().setCellHorizontalAlignment(bar, HasHorizontalAlignment.ALIGN_RIGHT);
		WidgetFactory.glue(getContent(), "5px");
		PropertyTable properties = new PropertyTable();
//		properties.setWidth("100%");
		properties.insertWidget("Title", task.getTitle());
		statusPanel.setWidget(WidgetFactory.getStatusLabel(task.getStatus()));
		properties.insertWidget("Status", statusPanel);
		properties.insertWidget("Description", task.getDescription());
		properties.insertWidget("Date start", CfgConstants.DATE_TIME_FORMAT.format(task.getDateStart()));
		properties.insertWidget("Date end", CfgConstants.DATE_TIME_FORMAT.format(task.getDateEnd()));
		
		
		deck.add(properties);
		
		deck.showWidget(0);
		addWidget(deck);
		WidgetFactory.glue(		getContent(), "10px");
		DisclosurePanel panel = new DisclosurePanel("Assigned users");
		panel.add(new TaskUsersTable(project, task, false));
		addWidget(panel);

	}

	@Override
	public void showInfo(Response response) {
	}

}
