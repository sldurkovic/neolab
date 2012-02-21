package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import java.util.Date;
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

public abstract class CalendarTasksTableDataProvider<E> extends
		AsyncDataProvider<E> {

	private final Logger log = Logger.getLogger(getClass().getName());
	private int uid;
	private Date date;
	private int pid;

	public CalendarTasksTableDataProvider(int uid, int pid, Date date) {
		this.uid = uid;
		this.date = date;
		this.pid = pid;
	}

	@Override
	protected void onRangeChanged(final HasData<E> display) {
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int length = range.getLength();
		// TablePage<?> page = getDataFromServer(start, length);
		ColumnSort sort = getColumnSort();
		if (sort != null) {
			Util.logFine(log, "Attempting tasks request: start=" + start
					+ " limit=" + length + " sort=" + sort+"uid="+uid+" pid="+pid+" date="+date);
			Injector.INSTANCE.getNeoService().requestTasksTableByDate(start,
					length, sort, uid, pid, date,
					new AbstractAsyncCallback<TablePage<Task>>() {
						public void success(TablePage<Task> result) {
							log.fine("result=" + result);
							ArrayList<E> list = (ArrayList<E>) result.getList();
							display.setRowCount(result.getTotal());
							display.setRowData(start, list);
						}
					});
		}
	}

	public abstract ColumnSort getColumnSort();

	public void setDate(Date date) {
		this.date = date;
	}

}