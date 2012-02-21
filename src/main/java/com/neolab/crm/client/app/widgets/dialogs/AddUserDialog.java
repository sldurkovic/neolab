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
import com.neolab.crm.client.app.events.ProjectUserChangeEvent;
import com.neolab.crm.client.app.events.NewProjectEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.tables.UsersTable;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.rpc.Response;

public class AddUserDialog extends FormDialog implements HasAsyncInformation{
	
	private UsersTable table;
	
	public interface AddActivityHandler{
		void addActivity(int pid, ArrayList<Integer> uids, HasAsyncInformation caller);
	}
	
	private int pid;
	
	public AddUserDialog(final int pid, final AddActivityHandler handler) {
		this.pid = pid;
		table = new UsersTable(4,true, false);
		Label label = new Label("Select users from the list:");
		label.addStyleName("formDialog-Title");
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
				handler.addActivity(pid, list, AddUserDialog.this);
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
		Injector.INSTANCE.getEventBus().fireEvent(new ProjectUserChangeEvent(pid));
	}
	

	
}
