package com.neolab.crm.client.mvp;

import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.client.mvp.BaseMVP.Presenter;
import com.neolab.crm.client.mvp.BaseMVP.View;
import com.neolab.crm.shared.domain.User;

public abstract class BaseView<P extends Presenter<?>> extends VerticalContainer implements View<P>{
	
	protected P presenter;
	protected User activeUser;
	
	public BaseView(P presenter){
		super(false);
		setPresenter(presenter);
		activeUser = Injector.INSTANCE.getApplication().getActiveUser();
	}
	
	public void setPresenter(P presenter) {
		this.presenter = presenter;
	}
	
}
