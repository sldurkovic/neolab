package com.neolab.crm.client.app.widgets.tables;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.TaskAssignedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.UserPreviewWidget;
import com.neolab.crm.client.app.widgets.tables.resources.NeoPagerResources;
import com.neolab.crm.client.app.widgets.tables.resources.NeoTableResources;
import com.neolab.crm.client.fwk.containers.FlowContainer;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

public class TaskUsersTable extends FlowContainer {

	private final Logger log = Logger.getLogger(getClass().getName());

	private int PAGE_SIZE = 5;

	private CellTable<User> table;
	private TaskUsersTableDataProvider<User> dataProvider;
	private HashMap<Integer, String> columns;
	private Task task;
	private Project project;
	private Set<User> selected;

	private boolean multi;
	
	public TaskUsersTable(Project project, Task task, boolean multi) {
		super(false);
		this.task = task;
		this.multi = multi;
		this.project = project;
		render();
		Injector.INSTANCE.getEventBus().addHandler(TaskAssignedEvent.TYPE, new TaskAssignedEvent.Handler<TaskAssignedEvent>() {
			@Override
			public void on(TaskAssignedEvent e) {
				dataProvider.onRangeChanged(table);
		}});
	}
	
	public TaskUsersTable(int pageSize) {
		super(false);
		PAGE_SIZE = pageSize;
		render();
	}

	protected void render() {
		columns = new HashMap<Integer, String>();
		columns.put(0, ColumnSort.FIRST_NAME);
		columns.put(1, ColumnSort.LAST_NAME);
		columns.put(2, ColumnSort.EMAIL);

		table = new CellTable<User>(PAGE_SIZE, NeoTableResources.INSTANCE);
		table.setLoadingIndicator(ImageFactory.smallLoading());

		// Create name column.
		TextColumn<User> firstNameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getFirstName();
			}
		};

		// Make the name column sortable.
		firstNameColumn.setSortable(true);

		// Create address column.
		TextColumn<User> addressColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getEmail();
			}
		};
		addressColumn.setSortable(true);

		TextColumn<User> lastNameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getLastName();
			}
		};

		// Make the name column sortable.
		lastNameColumn.setSortable(true);

		// Create address column.
		TextColumn<User> emailColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getEmail();
			}
		};
		emailColumn.setSortable(true);
		if(!multi){

			final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>(
					User.KEY_PROVIDER);
			table.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							new UserPreviewWidget(project, selectionModel.getSelectedObject(), 2);
						}
				});
		}else{
			   // Add a selection model so we can select cells.
		    final MultiSelectionModel<User> selectionModel = new MultiSelectionModel<User>(
		       User.KEY_PROVIDER);
		    table.setSelectionModel(selectionModel,
		        DefaultSelectionEventManager.<User> createCheckboxManager());
		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				
				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					selected = selectionModel.getSelectedSet();
				}
			});
		    
		
			// checkbox
			Column<User, Boolean> checkColumn = new Column<User, Boolean>(
					new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(User object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}
			};
			table.addColumn(checkColumn, "");
		}


		// Add the columns.
		table.addColumn(firstNameColumn, "First Name");
		table.addColumn(lastNameColumn, "Last Name");
		table.addColumn(emailColumn, "Email");

		// set dimensions
		table.setWidth("auto", true);
		table.setColumnWidth(firstNameColumn, 245.0, Unit.PX);
		table.setColumnWidth(lastNameColumn, 200.0, Unit.PX);
		table.setColumnWidth(emailColumn, 200.0, Unit.PX);

		dataProvider = new TaskUsersTableDataProvider<User>(task.getTid(), multi) {
			@Override
			public ColumnSort getColumnSort() {
				ColumnSort sort = null;
				if (table.getColumnSortList().size() > 0) {
					sort = new ColumnSort();
					sort.setColumn(columns.get(table
							.getColumnIndex((Column<User, ?>) table
									.getColumnSortList().get(0).getColumn())));
					sort.setAscending(table.getColumnSortList().get(0)
							.isAscending());
				}
				return sort;
			}
		};
		dataProvider.addDataDisplay(table);

		table.addColumnSortHandler(new AsyncHandler(table));
		table.getColumnSortList().push(firstNameColumn);

		// Create a Pager to control the table.
	    SimplePager pager = new SimplePager(TextLocation.CENTER, NeoPagerResources.INSTANCE, false, 0, false);
		pager.setDisplay(table);
		pager.setPageSize(PAGE_SIZE);

		dataProvider.onRangeChanged(table);
		// ADD TO CONTAINER
		addWidget(table);
		addWidget(pager);
	}

	public Set<User> getSelectedSet() {
		return selected;
	}
}
