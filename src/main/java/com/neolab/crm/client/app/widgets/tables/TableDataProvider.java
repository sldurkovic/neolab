package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.neolab.crm.client.utils.Util;
import com.neolab.crm.shared.resources.ColumnSort;
import com.neolab.crm.shared.resources.TablePage;

public abstract class TableDataProvider <E> extends AsyncDataProvider<E> {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	private int cid;
	private HasData<E> display;
	private CellTable<E> table;
	private HashMap<Integer, String> columns;
	public TableDataProvider(CellTable<E> table, HashMap<Integer, String> columns){
		this.table = table;
		this.columns = columns;
	}
	
	public TableDataProvider(int cid) {
		this.cid = cid;
	}
	
	@Override
	protected void onRangeChanged(final HasData<E> display) {
		this.display = display;
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int length = range.getLength();
//		TablePage<?> page = getDataFromServer(start, length);
		ColumnSort sort = getColumnSort();
		if(sort != null){
			Util.logFine(log,"Attempting request: start="+start+" limit="+length+" sort="+sort);
			request(start, length, sort, cid, display);
		}
	}
	
	public ColumnSort getColumnSort(){
		ColumnSort sort = null;
		if (table.getColumnSortList().size() > 0) {
			sort = new ColumnSort();
			sort.setColumn(columns.get(table
					.getColumnIndex((Column<E, ?>) table
							.getColumnSortList().get(0).getColumn())));
			sort.setAscending(table.getColumnSortList().get(0)
					.isAscending());
		}
		return sort;
	}
	public abstract void request(int start, int length, ColumnSort sort, int par, HasData<E> display2);
	public void response(int start, TablePage<E> result){
		log.fine("result="+result);
		ArrayList<E> list = (ArrayList<E>) result.getList();
		display.setRowCount(result.getTotal());
		display.setRowData(start, list);
	}
}