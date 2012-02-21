package com.neolab.crm.client.mvp.activities;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.events.ReloadActiveUserEvent;
import com.neolab.crm.client.app.events.SwitchTabEvent;
import com.neolab.crm.client.app.events.UserDeliveryEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.mvp.BasePresenter;
import com.neolab.crm.client.mvp.places.LoginPlace;
import com.neolab.crm.client.mvp.view.HomeView;
import com.neolab.crm.client.utils.Util;
import com.neolab.crm.shared.domain.News;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.TablePage;
import com.neolab.crm.shared.resources.rpc.ObjectResponse;
import com.neolab.crm.shared.resources.rpc.Response;
import com.neolab.crm.shared.resources.rpc.TablePageReponse;

public class HomeActivity extends BasePresenter<HomeView> {

	private final Logger log = Logger.getLogger(getClass().getName());

	public HomeActivity() {
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Util.logFine(log, "Starting activity HOME");
		User user = Injector.INSTANCE.getApplication().getActiveUser();
		if (user != null) {
			Injector.INSTANCE.getEventBus().fireEvent(new SwitchTabEvent(0));
			final HomeView view = Injector.INSTANCE.getViewFactory()
					.getHomeView(this);
			view.delivery(user);
			Injector.INSTANCE.getEventBus().addHandler(UserDeliveryEvent.TYPE,
					new UserDeliveryEvent.Handler<UserDeliveryEvent>() {
						@Override
						public void on(UserDeliveryEvent e) {
							view.delivery(e.getUser());
						}
					});
			setDisplay(view);
			panel.setWidget(display);
		} else {
			Injector.INSTANCE.getEventBus().fireEvent(
					new GoToPlaceEvent(new LoginPlace()));
		}

	}

	public void updateUser(User user) {
		Injector.INSTANCE.getNeoService().updateUser(user,
				new AbstractAsyncCallback<Response>() {
					@Override
					public void success(Response response) {
						display.getProfileWidget().showInfo(response);
					}
				});
	}

	public void requestUser(Requires<User> object) {
		User user = Injector.INSTANCE.getApplication().getActiveUser();
		if (user != null)
			object.delivery(user);
		else {
			// Injector.INSTANCE.getApplication().addUserRequest(object);
			Injector.INSTANCE.getEventBus().fireEvent(
					new ReloadActiveUserEvent());
		}
	}

	public void getFeed(int level, int pid, int start, int end,
			final Requires<TablePage<News>> caller) {
		int uid = Injector.INSTANCE.getApplication().getActiveUser().getUid();
		Injector.INSTANCE.getNeoService().getNews(level, pid,  uid, start, end,
				new AbstractAsyncCallback<TablePageReponse<News>>() {

					public void success(TablePageReponse<News> result) {
						log.fine(result + "");
						caller.delivery(result.getResult());
					};
				});
	}

	public void createNews(String title, String body, int level, int pid,
			final HasAsyncInformation caller) {
		Injector.INSTANCE.getNeoService().createNews(
				Injector.INSTANCE.getApplication().getActiveUser().getUid(),
				title, body, level, pid, new AbstractAsyncCallback<Response>() {
					@Override
					public void success(Response result) {
						caller.showInfo(result);
					}
				});
	}

	public void createProject(String title, int cid, String description,
			final Requires<Integer> caller) {
		Injector.INSTANCE.getNeoService().createProject(title, description, cid, new AbstractAsyncCallback<ObjectResponse<Integer>>() {
			public void success(ObjectResponse<Integer> result) {
				caller.delivery(result.getObject());
			};
		});
	}
}
