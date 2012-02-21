package com.neolab.crm.client.app.widgets.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.neolab.crm.client.app.events.TaskAssignedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.tables.TaskUsersTable;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.rpc.Response;

public class AddUsersToTaskDialog extends FormDialog implements HasAsyncInformation{
	
	private TaskUsersTable table;
	
	public interface AddTaskActivityHandler{
		void addTaskActivity(int tid, ArrayList<Integer> uids, HasAsyncInformation caller);
	}
	
	private Task task;
	
	public AddUsersToTaskDialog(final Task task, final AddTaskActivityHandler handler) {
		this.task = task;
		table = new TaskUsersTable(null, task, true);
		Label label = new Label("Select users from the list:");
		label.addStyleName("formDialog-Title");

		setGlassStyleName("glass-Second-Level");
		addStyleName("z-Index-10");
		addWidget(label);
		addWidget(table);
		
		HorizontalPanel bar = new HorizontalPanel();
		bar.add(WidgetFactory.getBigButton("Add", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ArrayList<Integer> list = new ArrayList<Integer>();
				Set<User> set =  table.getSelectedSet();
				Iterator<User> ite = set.iterator();
				while(ite.hasNext()){
					list.add(ite.next().getUid());
				}
				handler.addTaskActivity(task.getTid(), list, AddUsersToTaskDialog.this);
			}
		}));
		
		HTML glue = new HTML("&nbsp;");
		bar.add(glue);
		bar.setCellWidth(glue, "10px");
		
		bar.add(WidgetFactory.getBigButton("Cancel", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		}));
		

		addWidget(bar);
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}

	@Override
	public void showInfo(Response response) {
		WidgetFactory.alert("User(s) added!");
		hide();
		removeStyleName("z-Index-10");
		Injector.INSTANCE.getEventBus().fireEvent(new TaskAssignedEvent());
	}
	

	
}
