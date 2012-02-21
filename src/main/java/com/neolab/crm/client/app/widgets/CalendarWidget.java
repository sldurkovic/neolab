package com.neolab.crm.client.app.widgets;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.neolab.crm.client.app.events.PDFEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.tables.CalendarTasksTable;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.CfgConstants;

public class CalendarWidget extends VerticalContainer {

	private DatePicker picker;
	private CalendarTasksTable table;
	private User user;
	private int pid;
	private boolean center;

	public CalendarWidget(int pid, boolean center) {
		super(false);
		this.center = center;
		this.pid = pid;
		user = Injector.INSTANCE.getApplication().getActiveUser();
		render();
		Injector.INSTANCE.getEventBus().addHandler(PDFEvent.TYPE,
				new PDFEvent.Handler<PDFEvent>() {
					@Override
					public void on(PDFEvent e) {
						if(e.getUser() == -1){
						Window.Location.replace("/neolab/download?pid="
								+ e.getProject() + "&type=pdf&date="+CfgConstants.DATE_FORMAT.format(picker.getValue()));
						}else{
							Window.Location.replace("/neolab/download?uid="
									+ e.getProject() + "&type=pdf&date="+CfgConstants.DATE_FORMAT.format(picker.getValue()));
						}
					}
				});

	}

	@Override
	protected void render() {
		int uid = user.getUid();
		if (pid != -1)
			uid = 0;
		table = new CalendarTasksTable(uid, pid);
		picker = new DatePicker();
		picker.setValue(new Date());
		picker.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				table.filter(event.getValue());
			}
		});
		WidgetFactory.glue(getTopContainer(), "20px");
		addWidget(picker);
		getTopContainer().setCellWidth(picker, "100%");
		if (center)
			getTopContainer().setCellHorizontalAlignment(picker,
					HasHorizontalAlignment.ALIGN_CENTER);
		else
			getTopContainer().setCellHorizontalAlignment(picker,
					HasHorizontalAlignment.ALIGN_LEFT);
		// getTopContainer().addStyleName("date");

		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("calendar-Table");

		WidgetFactory.glue(panel, "10px");
		panel.add(table);
		addWidget(panel);
		getTopContainer().setWidth("100%");
	}

}
