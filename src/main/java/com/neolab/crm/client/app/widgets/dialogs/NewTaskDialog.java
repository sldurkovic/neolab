package com.neolab.crm.client.app.widgets.dialogs;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.neolab.crm.client.app.events.NewTaskEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.PropertyTable;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.tables.ProjectsTable;
import com.neolab.crm.client.app.widgets.tables.TableHandler;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.CfgConstants;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.rpc.Response;

public class NewTaskDialog extends FormDialog implements HasAsyncInformation,
		Requires<Project> {

	private Project project;
	private Label projectLabel;
	private FlexTable table;
	private NewTaskHandler handler;
	private DateBox startDate;
	private DateBox endDate;
	private TextBox startTime;
	private TextBox endTime;
	private TextArea desc;
	private TextBox title;

	public interface NewTaskHandler {
		void addNewTask(int pid, String title, String description, Date start,
				Date end, HasAsyncInformation caller);
	}

	public NewTaskDialog(Project project, NewTaskHandler handler) {
		this.handler = handler;
		this.project = project;
		table = new FlexTable();
		table.addStyleName("new-Task-Table");
		renderPanel();
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
		show();
	}

	private void renderPanel() {
		Label head = new Label("Create a new task");
		head.addStyleName("formDialog-Title");
		addWidget(head);

		PropertyTable ptable = new PropertyTable();
		ptable.setLightKeyColor();
		ptable.setWidth("500px");

		// first
		HorizontalPanel first = new HorizontalPanel();
		first.setWidth("100%");
		projectLabel = new Label(project.getTitle());
		first.add(projectLabel);
		Label another = new Label("Another project?");
		another.addStyleName("hiperlink");
		another.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				SelectProjectDialog spd = new SelectProjectDialog(project.getCid(), NewTaskDialog.this);
				spd.setWidth("700px");
			}
		});
		first.add(another);
		first.setCellHorizontalAlignment(another, HasHorizontalAlignment.ALIGN_RIGHT);
		ptable.insertWidget("Project: ", first);

		// second
		title = ptable.insertTextWidget("Title", "100%");

		// third
		desc = new TextArea();
		desc.setWidth("300px");
		desc.setHeight("100px");
		desc.addStyleName("neo-TextBox");
		ptable.insertWidget("Description", desc);
		// fourth
		HorizontalPanel fourth = new HorizontalPanel();
		startDate = new DateBox();
		startDate.setValue(new Date());
		startDate.setFormat(new DateBox.DefaultFormat(CfgConstants.DATE_FORMAT));
		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				endDate.setValue(event.getValue());
			}
		});
		startDate.addStyleName("neo-TextBox");

		startTime = new TextBox();
		startTime.setWidth("60px");
		startTime.setValue("HH:SS");
		startTime.addStyleName("neo-TextBox-Blur");
		startTime.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				if (startTime.getValue().equals("HH:SS")) {
					startTime.removeStyleName("neo-TextBox-Blur");
					startTime.setValue("");
				}
			}
		});
		startTime.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (startTime.getValue().equals("")) {
					startTime.addStyleName("neo-TextBox-Blur");
					startTime.setValue("HH:SS");
				}
			}
		});
		startTime.addStyleName("neo-TextBox");
		fourth.add(startDate);
		fourth.add(startTime);
		ptable.insertWidget("Start date: ", fourth);

		// fifth
		HorizontalPanel fifth = new HorizontalPanel();
		endDate = new DateBox();
		endDate.setFormat(new DateBox.DefaultFormat(CfgConstants.DATE_FORMAT));
		endDate.addStyleName("neo-TextBox");
		endTime = new TextBox();
		endTime.setWidth("60px");
		endTime.setValue("HH:SS");
		endTime.addStyleName("neo-TextBox-Blur");
		endTime.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				if (endTime.getValue().equals("HH:SS")) {
					endTime.removeStyleName("neo-TextBox-Blur");
					endTime.setValue("");
				}
			}
		});
		endTime.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (endTime.getValue().equals("")) {
					endTime.addStyleName("neo-TextBox-Blur");
					endTime.setValue("HH:SS");
				}
			}
		});
		endTime.addStyleName("neo-TextBox");

		fifth.add(endDate);
		fifth.add(endTime);
		ptable.insertWidget("End date:", fifth);

		HorizontalPanel bar = new HorizontalPanel();
		bar.add(WidgetFactory.getBigButton("Add", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(title.getText().isEmpty())
					WidgetFactory.error("Title cannot be empty");
				else{
					try{
						Date start = CfgConstants.DATE_TIME_FORMAT.parse(startDate.getTextBox().getText()+" "+startTime.getText());
						Date end = CfgConstants.DATE_TIME_FORMAT.parse(endDate.getTextBox().getText()+" "+endTime.getText());
						if(start.before(new Date())){
								WidgetFactory.error("Start date cannot be in the past");
								return;
						}
						if(start.after(end))
							WidgetFactory.error("End date cannot be before start date");
						else{
							handler.addNewTask(project.getPid(), title.getText(),
								desc.getText(), start, end, NewTaskDialog.this);
						}
					}catch(Exception e){
						WidgetFactory.error("Invalid date time format");
						GWT.log(startDate.getTextBox().getText()+" "+startTime.getText());
					}
				}

			}
		}));
		WidgetFactory.glue(bar);
		bar.add(WidgetFactory.getBigButton("Cancel", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		}));
		ptable.insertButton(bar);

		addWidget(ptable);

	
	}

	@Override
	public void showInfo(Response response) {
		WidgetFactory.info("Task added");
		Injector.INSTANCE.getEventBus().fireEvent(
				new NewTaskEvent(project.getPid()));
		hide();
	}

	@Override
	public void delivery(Project object) {
		project = object;
		projectLabel.setText(object.getTitle());
	}

}
