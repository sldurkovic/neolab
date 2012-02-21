package com.neolab.crm.client.app.widgets.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.tables.ProjectsTable;
import com.neolab.crm.client.app.widgets.tables.TableHandler;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.Requires;

public class SelectProjectDialog extends FormDialog{

	private Requires<Project> caller;
	private int cid;
	private Project selected;
	
	public SelectProjectDialog(int cid, Requires<Project> caller) {
		this.cid = cid;
		this.caller = caller;
		renderPanel();
		setGlassStyleName("glass-Second-Level");
		addStyleName("z-Index-10");
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}

	private void renderPanel() {
		ProjectsTable table = new ProjectsTable(cid, new TableHandler<Project>() {
			@Override
			public void onRowClick(Project object) {
				selected = object;
			}
		});
		addWidget(table);
		addWidget(WidgetFactory.getBigButton("Ok", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(selected == null)
					WidgetFactory.alert("You must select a project");
				caller.delivery(selected);
				hide();
				SelectProjectDialog.this.removeStyleName("z-Index-10");
			}
		}));
	}
	
}
