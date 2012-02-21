package com.neolab.crm.client.mvp.activities;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.events.SwitchTabEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.mvp.BasePresenter;
import com.neolab.crm.client.mvp.places.LoginPlace;
import com.neolab.crm.client.mvp.view.MembersView;
import com.neolab.crm.client.utils.Util;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.rpc.Response;

public class MembersActivity extends BasePresenter<MembersView>{
	
	private static final Logger log = Logger.getLogger(MembersActivity.class.getName());
	
	public MembersActivity() {
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Util.logFine(log, "Starting activity ADMIN");
		User user = Injector.INSTANCE.getApplication().getActiveUser();
		if(user != null){
			Injector.INSTANCE.getEventBus().fireEvent(new SwitchTabEvent(2));
			MembersView view = Injector.INSTANCE.getViewFactory().getAdminView(this);
			setDisplay(view);
			panel.setWidget(display);		
		}else{
			Injector.INSTANCE.getEventBus().fireEvent(new GoToPlaceEvent(new LoginPlace()));
		}

	}

	public void registerUser(String name, String email, int level, final HasAsyncInformation caller) {
		//do register
		Injector.INSTANCE.getNeoService().registerUser(name, email, level, new AbstractAsyncCallback<Response>() {
			@Override
			public void success(Response response) {
				caller.showInfo(response);
			}
		});
	}

	public void updateUsers(ArrayList<User> list, final HasAsyncInformation caller) {
		Injector.INSTANCE.getNeoService().updateUsers(list, new AbstractAsyncCallback<Response>() {
			@Override
			public void success(Response result) {
				caller.showInfo(result);
			}
		});
	}

//	public TablePage<User> requestUsersTable(int start, int limit, ColumnSort sort) {
//		Util.logFine(log,"Attempting request: start="+start+" limit="+limit+" sort="+sort);
//		final TablePage<User> page = new TablePage<User>();
//		Injector.INSTANCE.getNeoService().requestUsersTable(start, limit, sort, new AbstractAsyncCallback<TablePage<User>>() {
//			public void success(TablePage<User> result) {
//				log.fine("result="+result);
//				display.refreshUsersTable(result);
//			};
//		});
//		return page;
//	}

//	public void requestUsersTable(final Request request, final Callback<User> tableCallback) {
////		display.setLastPage(request.getStartRow()/10);
//		Injector.INSTANCE.getNeoService().requestUsers(request.getStartRow(), getColumnSort(request.getColumnSortList()),
//			new AbstractAsyncCallback<UsersPage>() {
//				@Override
//				public void success(final UsersPage result) {
//					log.fine("search result = " + result);
//					tableCallback.onRowsReady(request, new TableModelHelper.Response<User>() {
//						@Override
//						public Iterator<User> getRowValues() {
//							return result.getUsers().iterator();
//						}
//					});
//					display.setTotalNumberOfUsers(result.getTotalLength());
//				}
//
//			});
//	}

//	private ColumnSort getColumnSort(ColumnSortList columnSortList) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
