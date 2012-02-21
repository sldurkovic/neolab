package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.xpath.compiler.PsuedoNames;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.NewProjectEvent;
import com.neolab.crm.client.app.events.NewTaskEvent;
import com.neolab.crm.client.app.events.PDFEvent;
import com.neolab.crm.client.app.events.TaskUpdatedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.dialogs.TaskPreviewDialog;
import com.neolab.crm.client.app.widgets.tables.resources.NeoPagerResources;
import com.neolab.crm.client.app.widgets.tables.resources.NeoTableResources;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.fwk.containers.FlowContainer;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.resources.CfgConstants;
import com.neolab.crm.shared.resources.ColumnSort;

public class CalendarTasksTable extends FlowContainer implements HasFilter<Date>{

	private final Logger log = Logger.getLogger(getClass().getName());

	private static int PAGE_SIZE = 10;

	private CellTable<Task> table;
	private CalendarTasksTableDataProvider<Task> dataProvider;
	private HashMap<Integer, String> columns;
	private int uid;
	private Date date;

	private int pid;

	public CalendarTasksTable(int uid, int pid) {
		super(false);
		this.pid = pid;
		date = new Date();
		this.uid = uid;
		render();
		Injector.INSTANCE.getEventBus().addHandler(TaskUpdatedEvent.TYPE, new TaskUpdatedEvent.Handler<TaskUpdatedEvent>() {
			@Override
			public void on(TaskUpdatedEvent e) {
				dataProvider.onRangeChanged(table);
		}});
		Injector.INSTANCE.getEventBus().addHandler(NewTaskEvent.TYPE, new NewTaskEvent.Handler<NewTaskEvent>() {
			@Override
			public void on(NewTaskEvent e) {
				dataProvider.onRangeChanged(table);
		}});
	}

	protected void render() {
		columns = new HashMap<Integer, String>();
		columns.put(0, ColumnSort.TITLE);
		columns.put(1, ColumnSort.DATE_START);
		columns.put(2, ColumnSort.DATE_END);
		columns.put(3, ColumnSort.TASK_STATUS);

		table = new CellTable<Task>(PAGE_SIZE, NeoTableResources.INSTANCE);
		table.setLoadingIndicator(ImageFactory.smallLoading());
		table.setEmptyTableWidget(new Label("Empty list"));
		// Create name column.
		TextColumn<Task> titleColum = new TextColumn<Task>() {
			@Override
			public String getValue(Task task) {
				return task.getTitle();
			}
		};

		// Make the name column sortable.
		titleColum.setSortable(true);

		Column<Task, Date> dateStart = new Column<Task, Date>(new DateCell(
				CfgConstants.DATE_TIME_FORMAT)) {
			@Override
			public Date getValue(Task object) {
				return object.getDateStart();
				
			}

		};

		// Make the name column sortable.
		dateStart.setSortable(true);
		Column<Task, Date> dateEnd = new Column<Task, Date>(new DateCell(
				CfgConstants.DATE_TIME_FORMAT)) {
			@Override
			public Date getValue(Task object) {
				return object.getDateEnd();
			}

		};
		

		// Make the name column sortable.
		dateEnd.setSortable(true);
		Column<Task, SafeHtml> statusColumn = new Column<Task, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Task object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String status = object.getStatus();
				if (status.equals("ACTIVE"))
					sb.appendHtmlConstant("<span style=\"color:red\">");
				else
					sb.appendHtmlConstant("<span style=\"color:green\">");
				sb.appendHtmlConstant(status);
				if (status.equals("ACTIVE"))
					sb.appendHtmlConstant("</span>");
				return sb.toSafeHtml();

			}
		};
		// // Make the name column sortable.
		statusColumn.setSortable(true);

			final SingleSelectionModel<Task> selectionModel = new SingleSelectionModel<Task>(
					Task.KEY_PROVIDER);
			table.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
//							handler.onRowClick(selectionModel.getSelectedObject());
						}
					});

			//on row click
			table.addHandler(new CellPreviewEvent.Handler<Task>() {
				@Override
				public void onCellPreview(CellPreviewEvent<Task> event) {
					boolean isClick = "click".equals(event.getNativeEvent().getType());
					if(isClick)
//						Window.alert("Task clicked");
						new TaskPreviewDialog(selectionModel.getSelectedObject());
				}
			}, CellPreviewEvent.getType());
		    
		
		// Add the columns.
		table.addColumn(titleColum, "Title");
		table.addColumn(dateStart, "Date start");
		table.addColumn(dateEnd, "Date end");
		table.addColumn(statusColumn, "Status");

		// set dimensions
		table.setWidth("100%");
//		table.setColumnWidth(titleColum, 245.0, Unit.PX);

		dataProvider = new CalendarTasksTableDataProvider<Task>(uid, pid, date) {
			// @Override
			// public TablePage<User> getDataFromServer(int start, int limit) {
			// ColumnSort sort = null;
			// if(table.getColumnSortList().size() > 0){
			// sort = new ColumnSort();
			// sort.setColumn(columns.get(table.getColumnIndex((Column<User, ?>)
			// table.getColumnSortList().get(0).getColumn())));
			// sort.setAscending(table.getColumnSortList().get(0).isAscending());
			// }
			// return requestTableData(start, limit, sort);
			// }

			@Override
			public ColumnSort getColumnSort() {
				ColumnSort sort = null;
				if (table.getColumnSortList().size() > 0) {
					sort = new ColumnSort();
					sort.setColumn(columns.get(table
							.getColumnIndex((Column<Task, ?>) table
									.getColumnSortList().get(0).getColumn())));
					sort.setAscending(table.getColumnSortList().get(0)
							.isAscending());
				}
				return sort;
			}
		};
		dataProvider.addDataDisplay(table);

		table.addColumnSortHandler(new AsyncHandler(table));
		table.getColumnSortList().push(titleColum);

		
		// Create a Pager to control the table.
	    SimplePager pager = new SimplePager(TextLocation.CENTER, NeoPagerResources.INSTANCE, false, 0, false);
		pager.setDisplay(table);
		pager.setPageSize(PAGE_SIZE);

		dataProvider.onRangeChanged(table);

		// ADD TO CONTAINER
		PushButton all = new PushButton("Show all tasks", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				filter(null);
			}
		});
		
		PushButton pdf = new PushButton("Download PDF", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(pid != -1)
					Injector.INSTANCE.getEventBus().fireEvent(new PDFEvent(pid, -1));
				else
					Injector.INSTANCE.getEventBus().fireEvent(new PDFEvent(-1, uid));
			}
		});
		HorizontalPanel bar = new HorizontalPanel();
		bar.add(pager);
		WidgetFactory.glue(bar);
		bar.add(all);
		WidgetFactory.glue(bar);
		bar.add(pdf);
		addWidget(bar);
		addWidget(table);
	}

	public void filter(Date date) {
		this.date = date;
		dataProvider.setDate(date);
		dataProvider.onRangeChanged(table);
	}
	
}