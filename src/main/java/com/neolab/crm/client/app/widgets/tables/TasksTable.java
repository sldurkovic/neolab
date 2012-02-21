package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.NewTaskEvent;
import com.neolab.crm.client.app.events.TaskUpdatedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.tables.resources.NeoPagerResources;
import com.neolab.crm.client.app.widgets.tables.resources.NeoTableResources;
import com.neolab.crm.client.fwk.containers.FlowContainer;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.resources.CfgConstants;
import com.neolab.crm.shared.resources.ColumnSort;

public class TasksTable extends FlowContainer {

	private final Logger log = Logger.getLogger(getClass().getName());

	private static int PAGE_SIZE = 3;

	private CellTable<Task> table;
	private TasksTableDataProvider<Task> dataProvider;
	private HashMap<Integer, String> columns;
	private int pid;
	private TableHandler<Task> handler;
	private boolean multi;
	private Set<Task> selected;
	private int uid;
	private boolean reverse;

	public TasksTable(final int pid, int uid, boolean reverse, boolean multiSelect, TableHandler<Task> handler) {
		super(false);
		this.uid = uid;
		this.reverse = reverse;
		this.multi = multiSelect;
		this.handler = handler;
		this.pid = pid;
		Injector.INSTANCE.getEventBus().addHandler(NewTaskEvent.TYPE, new NewTaskEvent.Handler<NewTaskEvent>() {
			@Override
			public void on(NewTaskEvent e) {
				if(pid == e.getProject()){
					dataProvider.onRangeChanged(table);
				}
			}
		});
		render();
		Injector.INSTANCE.getEventBus().addHandler(TaskUpdatedEvent.TYPE, new TaskUpdatedEvent.Handler<TaskUpdatedEvent>() {
			@Override
			public void on(TaskUpdatedEvent e) {
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
		table.setWidth("100%");
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

		if(!multi){
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
						handler.onRowClick(selectionModel.getSelectedObject());
				}
			}, CellPreviewEvent.getType());
		}else{
		    // Add a selection model so we can select cells.
		    final MultiSelectionModel<Task> selectionModel = new MultiSelectionModel<Task>(
		    		Task.KEY_PROVIDER);
		    table.setSelectionModel(selectionModel,
		        DefaultSelectionEventManager.<Task> createCheckboxManager());
		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				
				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					selected = selectionModel.getSelectedSet();
				}
			});
		    
		
			// checkbox
			Column<Task, Boolean> checkColumn = new Column<Task, Boolean>(
					new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(Task object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}
			};
			table.addColumn(checkColumn, "");
		}

		
		// Add the columns.
		table.addColumn(titleColum, "Title");
		table.addColumn(dateStart, "Date start");
		table.addColumn(dateEnd, "Date end");
		table.addColumn(statusColumn, "Status");

		// set dimensions
		table.setWidth("auto", true);
		table.setColumnWidth(titleColum, 245.0, Unit.PX);

		dataProvider = new TasksTableDataProvider<Task>(pid, uid, reverse) {
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
		addWidget(table);
		addWidget(pager);
	}

	public void filter(int pid) {
		dataProvider.setPid(pid);
		dataProvider.onRangeChanged(table);
	}
	
	public ArrayList<Integer> getSelectedSet(){
		if(selected != null){
			ArrayList<Integer> list = new ArrayList<Integer>();
			Iterator<Task> ite = selected.iterator();
			while(ite.hasNext()){
				list.add(ite.next().getTid());
			}
			return list;
		}
		return null;
	}
	
	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		table.setWidth(width);
	}
}