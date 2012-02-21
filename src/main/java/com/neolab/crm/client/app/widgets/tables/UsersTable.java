package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.UserPreviewWidget;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.tables.resources.NeoPagerResources;
import com.neolab.crm.client.app.widgets.tables.resources.NeoTableResources;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

public class UsersTable extends VerticalContainer {

	private final Logger log = Logger.getLogger(getClass().getName());

//	private static int PAGE_SIZE = 3;
	private int PAGE_SIZE = 5;

	private CellTable<User> table;
	private UsersTableDataProvider<User> dataProvider;
	private HashMap<Integer, String> columns;
	
	private Set<User> selected;

	private boolean multi;

	private boolean detailed;
	private ArrayList<PushButton> buttons;
	
	public UsersTable(boolean multi) {
		super(false);
		this.multi = multi;
		render();
	}
	
	public UsersTable(int pageSize, boolean multi, boolean detailed) {
		super(false);
		this.detailed = detailed;
		PAGE_SIZE = pageSize;
		this.multi = multi;
		render();
	}
	
	public UsersTable(int pageSize, boolean multi, boolean detailed, ArrayList<PushButton> buttons) {
		super(false);
		this.detailed = detailed;
		this.buttons = buttons;
		PAGE_SIZE = pageSize;
		this.multi = multi;
		render();
	}

	protected void render() {
		columns = new HashMap<Integer, String>();
		columns.put(0, ColumnSort.FIRST_NAME);
		columns.put(1, ColumnSort.LAST_NAME);
		columns.put(2, ColumnSort.EMAIL);
		columns.put(3, ColumnSort.LEVEL);

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
							new UserPreviewWidget(selectionModel.getSelectedObject());
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
		table.setWidth("100%");
//		table.setColumnWidth(firstNameColumn, 245.0, Unit.PX);
//		table.setColumnWidth(lastNameColumn, 200.0, Unit.PX);
//		table.setColumnWidth(emailColumn, 200.0, Unit.PX);

		if(detailed)
			showDetails();
		
		dataProvider = new UsersTableDataProvider<User>() {
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
	    SimplePager pager = new SimplePager(TextLocation.CENTER,NeoPagerResources.INSTANCE,false, 0, false);
		pager.setDisplay(table);
		pager.setPageSize(PAGE_SIZE);

		dataProvider.onRangeChanged(table);
		// ADD TO CONTAINER
		
		if(buttons != null){
			HorizontalPanel bar = new HorizontalPanel();
			bar.add(pager);
			HorizontalPanel bbar = new HorizontalPanel();
			for (PushButton but : buttons) {
				bbar.add(but);
				WidgetFactory.glue(bbar);
			}
			WidgetFactory.glue(bar, "50px", "");
			bar.add(bbar);
			addWidget(bar);
		}else
			addWidget(pager);
		addWidget(table);
	}
	
	public Set<User> getSelectedSet(){
		return selected;
	}
	
	private void showDetails(){
		// Create address column.
		TextColumn<User> phoneColumn = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getPhone();
			}
		};
		phoneColumn.setSortable(false);
		
		// Create address column.
		TextColumn<User> webSite = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return user.getSite();
			}
		};
		webSite.setSortable(false);
		
		// Create address column.
		TextColumn<User> position = new TextColumn<User>() {
			@Override
			public String getValue(User user) {
				return Injector.INSTANCE.getApplication().getLevelName(user.getLevel());
			}
		};
		position.setSortable(true);

		table.addColumn(position, "Role");
		table.addColumn(phoneColumn, "Phone");
		table.addColumn(webSite, "Site");
		
		
	}


	// @Override
	// public TablePage<User> requestTableData(int start, int limit, ColumnSort
	// sort) {
	// throw new RuntimeException("Request not implemented");
	// }
}
