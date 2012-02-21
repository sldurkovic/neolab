package com.neolab.crm.client.app.widgets.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.neolab.crm.client.app.events.NewTaskEvent;
import com.neolab.crm.client.app.events.ProjectUpdatedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.mvp.activities.ProjectsActivity;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.rpc.Response;

public class EditProjectDialog extends FormDialog implements HasAsyncInformation{

	private Project project;
	private FlexTable table;
	private ProjectsActivity presenter;

	public EditProjectDialog(Project project, ProjectsActivity presenter) {
		this.project = project;
		this.presenter = presenter;
		renderPanel();
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}

	private void renderPanel() {
		
		Label head = new Label("Edit properties");
		head.addStyleName("formDialog-Title");
		addWidget(head);
		
		table = new FlexTable();
		
		Label tlabel = new Label("Title: ");
		table.setWidget(0, 0, tlabel);

		final TextBox tbox = new TextBox();
		tbox.setText(project.getTitle());
		tbox.addStyleName("neo-TextBox");

		table.setWidget(0, 1, tbox);
		
		Label dlabel = new Label("Description: ");
		table.setWidget(1, 0, dlabel);

		final TextArea desc = new TextArea();
		desc.setValue(project.getDescription());
		desc.setWidth("300px");
		desc.setHeight("100px");
		desc.addStyleName("neo-TextBox");
		table.setWidget(1, 1, desc);
		
		HorizontalPanel bar = new HorizontalPanel();
		bar.add(WidgetFactory.getBigButton("Save", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				project.setTitle(tbox.getText());
				project.setDescription(desc.getValue());
				presenter.updateProject(project, EditProjectDialog.this);
			}
		}));
		WidgetFactory.glue(bar);
		bar.add(WidgetFactory.getBigButton("Cancel", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		}));
		
		table.getFlexCellFormatter().setColSpan(2, 0, 2);
		table.setWidget(2, 0, bar);
		table.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);

		
		addWidget(table);
		
	}

	@Override
	public void showInfo(Response response) {
		WidgetFactory.notify(response);
		if(response.getStatus()){
			Injector.INSTANCE.getEventBus().fireEvent(
					new ProjectUpdatedEvent(project.getPid()));
			hide();
		}
	}
	
}
