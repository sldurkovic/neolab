package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.utils.Util;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;
import com.neolab.crm.shared.resources.TablePage;

public abstract class TaskUsersTableDataProvider<E> extends AsyncDataProvider<E> {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	
	private int tid;

	private boolean reverse;
	public TaskUsersTableDataProvider(int tid, boolean reverse){
		this.tid = tid;
		this.reverse = reverse;
	}
	
	@Override
	protected void onRangeChanged(final HasData<E> display) {
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int length = range.getLength();
//		TablePage<?> page = getDataFromServer(start, length);
		ColumnSort sort = getColumnSort();
		if(sort != null){
			Util.logFine(log,"Attempting request: start="+start+" limit="+length+" sort="+sort);
			
				Injector.INSTANCE.getNeoService().requestUsersTableByTask(start, length, sort, tid, reverse, new AbstractAsyncCallback<TablePage<User>>() {
					public void success(TablePage<User> result) {
						log.fine("result="+result);
						ArrayList<E> list = (ArrayList<E>) result.getList();
						display.setRowCount(result.getTotal());
						display.setRowData(start, list);
					}
				});	
		}
//		ArrayList<E> list = (ArrayList<E>) page.getList();
//		display.setRowCount(page.getTotal());
//		display.setRowData(0, list);
	}
	
	public abstract ColumnSort getColumnSort();
	
}