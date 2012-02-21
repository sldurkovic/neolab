package com.neolab.crm.client.app.widgets.tables;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.NewProjectEvent;
import com.neolab.crm.client.app.events.ProjectUpdatedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.projects.ProjectHolder;
import com.neolab.crm.client.app.widgets.tables.resources.NeoPagerResources;
import com.neolab.crm.client.app.widgets.tables.resources.NeoTableResources;
import com.neolab.crm.client.fwk.containers.FlowContainer;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.CfgConstants;
import com.neolab.crm.shared.resources.ColumnSort;

public class ProjectsTable extends FlowContainer{

	private final Logger log = Logger.getLogger(getClass().getName());
	
	private static int PAGE_SIZE = 6;
	
	private CellTable<Project> table;
	private ProjectsTableDataProvider<Project> dataProvider;
	private HashMap<Integer, String> columns;
	private int cid;
	private TableHandler<Project> handler;
	
	public ProjectsTable(int cid, TableHandler<Project> handler){
		super(false);
		addStyleName("projects-Table");
		this.handler = handler;
		this.cid = cid;
		render();
		
		Injector.INSTANCE.getEventBus().addHandler(NewProjectEvent.TYPE, new NewProjectEvent.Handler<NewProjectEvent>() {
			@Override
			public void on(NewProjectEvent e) {
				dataProvider.onRangeChanged(table);
		}});
	}

	protected void render() {
		columns = new HashMap<Integer, String>();
		columns.put(0, ColumnSort.TITLE);
		columns.put(2, ColumnSort.DATE_CREATED);
		table = new CellTable<Project>(PAGE_SIZE, NeoTableResources.INSTANCE);

		table.setLoadingIndicator(ImageFactory.smallLoading());
		 // Create name column.
	    TextColumn<Project> titleColum = new TextColumn<Project>() {
	      @Override
	      public String getValue(Project project) {
	        return project.getTitle();
	      }
	    };

	    // Make the name column sortable.
	    titleColum.setSortable(true);

	    TextColumn<Project> descColumn = new TextColumn<Project>() {
		      @Override
		      public String getValue(Project project) {
		        return project.getDescription();
		      }
		    };

		    // Make the name column sortable.
		    descColumn.setSortable(false);
	    

		 // Create name column.
	    Column<Project, Date> dateColumn = new Column<Project, Date>(new DateCell(CfgConstants.DATE_FORMAT)) {
			@Override
			public Date getValue(Project object) {
			    return object.getDateCreated();
			}
	     
	    };

	    // Make the name column sortable.
	    dateColumn.setSortable(true);
	    
		final SingleSelectionModel<Project> selectionModel = new SingleSelectionModel<Project>(
		            Project.KEY_PROVIDER);
		        table.setSelectionModel(selectionModel);
		        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		          public void onSelectionChange(SelectionChangeEvent event) {
		        	  handler.onRowClick(selectionModel.getSelectedObject());
		          }
		        });
		    
	    // Add the columns.
	    table.addColumn(titleColum, "Title");
	    table.addColumn(descColumn, "Description");
	    table.addColumn(dateColumn, "Date Created");
	    
	    //set dimensions
	    table.setWidth("auto", true);
	    table.setColumnWidth(titleColum, 245.0, Unit.PX);
	    
	    dataProvider = new ProjectsTableDataProvider<Project>(cid){
//	    	@Override
//	    	public TablePage<User> getDataFromServer(int start, int limit) {
//	    		ColumnSort sort = null;
//	    		if(table.getColumnSortList().size() > 0){
//	    			sort  = new ColumnSort();
//	    			sort.setColumn(columns.get(table.getColumnIndex((Column<User, ?>) table.getColumnSortList().get(0).getColumn())));
//	    			sort.setAscending(table.getColumnSortList().get(0).isAscending());
//	    		}
//	    		return requestTableData(start, limit, sort);
//	    	}

			@Override
			public ColumnSort getColumnSort() {
	    		ColumnSort sort = null;
	    		if(table.getColumnSortList().size() > 0){
	    			sort  = new ColumnSort();
	    			sort.setColumn(columns.get(table.getColumnIndex((Column<Project, ?>) table.getColumnSortList().get(0).getColumn())));
	    			sort.setAscending(table.getColumnSortList().get(0).isAscending());
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
	    setWidth("100%");
	    table.setWidth("100%");
	}
}