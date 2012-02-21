package com.neolab.crm.client.app.base;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.neolab.crm.client.app.widgets.WaitPanel;
import com.neolab.crm.client.utils.Util;
	
public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T>{
	
	private static final Logger log = Logger.getLogger(AbstractAsyncCallback.class.getName());
	
	public AbstractAsyncCallback(){
//		Util.logFine(log, "Async activity START");
//		WaitPanel.wait(true);
	}

	@Override
	public final void onFailure(Throwable caught) {
//		Util.logFine(log, "Async activity STOP");
		Util.logWarn(log, "Request Failed on the server: "+caught);
//		WaitPanel.wait(false);
	}

	@Override
	public final void onSuccess(T result) {
//		Util.logFine(log, "Async activity STOP");
//		WaitPanel.wait(false);
		success(result);
	}

	public abstract void success(T result);
	

}
