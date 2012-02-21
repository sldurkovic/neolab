package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.utils.Util;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.resources.ColumnSort;
import com.neolab.crm.shared.resources.TablePage;

public abstract class TasksTableDataProvider<E> extends AsyncDataProvider<E> {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	private int pid;
	private int uid;
	private boolean reverse;
	
	public TasksTableDataProvider(int pid, int uid, boolean reverse) {
		this.pid = pid;
		this.uid = uid;
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
			Util.logFine(log,"Attempting tasks request: start="+start+" limit="+length+" sort="+sort+" pid="+pid );
			if(uid == -1){
				Injector.INSTANCE.getNeoService().requestTasksTable(start, length, sort, pid, new AbstractAsyncCallback<TablePage<Task>>() {
					public void success(TablePage<Task> result) {
						log.fine("result="+result);
						ArrayList<E> list = (ArrayList<E>) result.getList();
						display.setRowCount(result.getTotal());
						display.setRowData(start, list);
					}
				});
			}else{
				Injector.INSTANCE.getNeoService().getUserTasksTable(start, length, sort, pid, uid, reverse, new AbstractAsyncCallback<TablePage<Task>>() {
					public void success(TablePage<Task> result) {
						log.fine("result="+result);
						ArrayList<E> list = (ArrayList<E>) result.getList();
						display.setRowCount(result.getTotal());
						display.setRowData(start, list);
					}
				});
			}
		}
//		ArrayList<E> list = (ArrayList<E>) page.getList();
//		display.setRowCount(page.getTotal());
//		display.setRowData(0, list);
	}
	
	public abstract ColumnSort getColumnSort();
	
	public void setPid(int pid){
		this.pid = pid;
	}
	
}