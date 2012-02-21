package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.tables.resources.NeoPagerResources;
import com.neolab.crm.client.app.widgets.tables.resources.NeoTableResources;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;
import com.neolab.crm.shared.resources.rpc.Response;

public class EditUsersTable extends VerticalContainer implements HasAsyncInformation{

	private final Logger log = Logger.getLogger(getClass().getName());
	
	public interface UpdateUserProvider{
		void update(ArrayList<User> list, HasAsyncInformation caller);
	}
	
//	private static int PAGE_SIZE = 3;
	private int PAGE_SIZE = 5;

	private CellTable<User> table;
	private UsersTableDataProvider<User> dataProvider;
	private HashMap<Integer, String> columns;
	private Set<User> selected;
	private boolean multi;
	private boolean detailed;
	private ArrayList<PushButton> buttons;
	private ArrayList<User> update = new ArrayList<User>();
	private UpdateUserProvider provider;

	public EditUsersTable(boolean multi) {
		super(false);
		this.multi = multi;
		render();
	}
	
	public EditUsersTable(int pageSize, boolean multi, boolean detailed) {
		super(false);
		this.detailed = detailed;
		PAGE_SIZE = pageSize;
		this.multi = multi;
		render();
	}
	
	public EditUsersTable(int pageSize, boolean multi, boolean detailed, ArrayList<PushButton> buttons, UpdateUserProvider provider) {
		super(false);
		this.provider = provider;
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
		Column<User, String> firstNameColumn = new Column<User, String>(new EditTextCell()) {
			@Override
			public String getValue(User user) {
				return user.getFirstName();
			}
		};
		
		  firstNameColumn.setFieldUpdater(new FieldUpdater<User, String>() {
		        public void update(int index, User object, String value) {
		          object.setFirstName(value);
		          addUserToUpdate(object);
		        }

		      });

		// Make the name column sortable.
		firstNameColumn.setSortable(true);

		// Create address column.
		Column<User, String> emailColumn = new Column<User, String>(new EditTextCell()) {
			@Override
			public String getValue(User user) {
				return user.getEmail();
			}
		};
		emailColumn.setFieldUpdater(new FieldUpdater<User, String>() {
		        public void update(int index, User object, String value) {
		          object.setEmail(value);
		          addUserToUpdate(object);
		        }

		      });
		emailColumn.setSortable(true);

		Column<User, String> lastNameColumn = new Column<User, String>(new EditTextCell()) {
			@Override
			public String getValue(User user) {
				return user.getLastName();
			}
		};
		lastNameColumn.setFieldUpdater(new FieldUpdater<User, String>() {
	        public void update(int index, User object, String value) {
	          object.setLastName(value);
	          addUserToUpdate(object);
	        }

	      });
		// Make the name column sortable.
		lastNameColumn.setSortable(true);

		if(!multi){
			final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>(
					User.KEY_PROVIDER);
			table.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
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
		Panel p = WidgetFactory.getBigButton("Save", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				provider.update(update, EditUsersTable.this);
			}
		});
		addWidget(p);
	}
	
	public Set<User> getSelectedSet(){
		return selected;
	}
	
	private void showDetails(){
		// Create address column.
		Column<User, String> phoneColumn = new Column<User, String>(new EditTextCell()) {
			@Override
			public String getValue(User user) {
				return user.getPhone();
			}
		};
		
		phoneColumn.setFieldUpdater(new FieldUpdater<User, String>() {
	        public void update(int index, User object, String value) {
	          object.setPhone(value);
	          addUserToUpdate(object);
	        }

	      });
		phoneColumn.setSortable(false);
		
		// Create address column.
		Column<User, String> webSite = new Column<User, String>(new EditTextCell()) {
			@Override
			public String getValue(User user) {
				return user.getSite();
			}
		};
		webSite.setFieldUpdater(new FieldUpdater<User, String>() {
	        public void update(int index, User object, String value) {
	          object.setSite(value);
	          addUserToUpdate(object);
	        }

	      });
		webSite.setSortable(false);
		
		// Create address column.
	    List<String> levels = new ArrayList<String>();
	    ArrayList<String> l = Injector.INSTANCE.getApplication().getLevels();
		
		for (String string : l) {
			levels.add(string);
		}
	    SelectionCell categoryCell = new SelectionCell(levels);
		Column<User,String> position = new Column<User, String>(categoryCell) {
			@Override
			public String getValue(User user) {
				return Injector.INSTANCE.getApplication().getLevelName(user.getLevel());
			}
		};
		position.setFieldUpdater(new FieldUpdater<User, String>() {
	        public void update(int index, User object, String value) {
	          object.setLevel(Injector.INSTANCE.getApplication().getLevelNumber(value));
	          addUserToUpdate(object);
	        }

	      });
		
		position.setSortable(true);

		table.addColumn(position, "Role");
		table.addColumn(phoneColumn, "Phone");
		table.addColumn(webSite, "Site");
		
		
	}

	private void addUserToUpdate(User object) {
		for (User user : update) {
			if(user.getUid() == object.getUid())
				return;
		}
		update.add(object);
	}

	@Override
	public void showInfo(Response response) {
		WidgetFactory.notify(response);
		if(response.getStatus())
			update.clear();
	}

	// @Override
	// public TablePage<User> requestTableData(int start, int limit, ColumnSort
	// sort) {
	// throw new RuntimeException("Request not implemented");
	// }
}
