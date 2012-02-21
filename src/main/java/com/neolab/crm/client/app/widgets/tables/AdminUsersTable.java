package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
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
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.neolab.crm.client.app.widgets.UserPreviewWidget;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.tables.resources.NeoPagerResources;
import com.neolab.crm.client.app.widgets.tables.resources.NeoTableResources;
import com.neolab.crm.client.fwk.containers.FlowContainer;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

public class AdminUsersTable extends FlowContainer{

	private final Logger log = Logger.getLogger(getClass().getName());
	
	private static int PAGE_SIZE = 3;
	
	private CellTable<User> table;
	private TableDataProvider<User> dataProvider;
	private HashMap<Integer, String> columns;
	private TableRequestHandler<User> handler;
	private ArrayList<User> edited = new ArrayList<User>();
	
	public interface TableRequestHandler<E>{
		void request(int start, int length, ColumnSort sort, int par, HasData<User> display);
		void update(List<E> objects);
	}
	
	public AdminUsersTable(TableRequestHandler<User> handler){
		super(false);
		this.handler = handler;
		render();
	}

	protected void render() {
		columns = new HashMap<Integer, String>();
		columns.put(0, ColumnSort.FIRST_NAME);
		columns.put(1, ColumnSort.LAST_NAME);
		columns.put(2, ColumnSort.PASSWORD);
		columns.put(3, ColumnSort.EMAIL);

		table = new CellTable<User>(PAGE_SIZE, NeoTableResources.INSTANCE);
		 // Create name column.
	    Column<User, String> firstNameColumn = new Column<User, String>(new EditTextCell()) {
	      @Override
	      public String getValue(User user) {
	        return user.getLastName();
	      }
	    };

	    firstNameColumn.setFieldUpdater(new FieldUpdater<User, String>() {
	        public void update(int index, User object, String value) {
		         Window.alert("sa");
	          object.setFirstName(value);
	        }
	      });
	      
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

		    // Create address column.
		    TextColumn<User> passwordColumn = new TextColumn<User>() {
		      @Override
		      public String getValue(User user) {
		        return user.getPassword();
		      }
		    };
		    passwordColumn.setSortable(true);


	    
		final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>(
		            User.KEY_PROVIDER);
		        table.setSelectionModel(selectionModel);
		        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		          public void onSelectionChange(SelectionChangeEvent event) {
		        	  new UserPreviewWidget(selectionModel.getSelectedObject());
		          }
		        });
		    
	    // Add the columns.
	    table.addColumn(firstNameColumn, "First Name");
	    table.addColumn(lastNameColumn, "Last Name");
	    table.addColumn(emailColumn, "Email");
	    table.addColumn(passwordColumn, "Password");
	    
	    //set dimensions
	    table.setWidth("auto", true);
	    table.setColumnWidth(firstNameColumn, 245.0, Unit.PX);
	    table.setColumnWidth(lastNameColumn, 200.0, Unit.PX);
	    table.setColumnWidth(emailColumn, 200.0, Unit.PX);
	    
	    dataProvider = new TableDataProvider<User>(-1){
			@Override
			public ColumnSort getColumnSort() {
	    		ColumnSort sort = null;
	    		if(table.getColumnSortList().size() > 0){
	    			sort  = new ColumnSort();
	    			sort.setColumn(columns.get(table.getColumnIndex((Column<User, ?>) table.getColumnSortList().get(0).getColumn())));
	    			sort.setAscending(table.getColumnSortList().get(0).isAscending());
	    		}
	    		return sort;
			}
			@Override
			public void request(int start, int length, ColumnSort sort,
					int par, HasData<User> display) {
				handler.request(start, length, sort, par, display);
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
	    addWidget(pager);
	    addWidget(table);
	    addWidget(WidgetFactory.getBigButton("Save changes", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
			}
		}));
	}


//	@Override
//	public TablePage<User> requestTableData(int start, int limit, ColumnSort sort) {
//		throw new RuntimeException("Request not implemented");
//	}
}
