package com.neolab.crm.shared.resources.rpc;

import com.neolab.crm.shared.resources.TablePage;

@SuppressWarnings("serial")
public class TablePageReponse<T> extends Response{

	private TablePage<T> result;
	
	public TablePageReponse() {
		super();
	}

	public TablePageReponse(boolean status) {
		super(status);
	}
	
	public TablePageReponse(boolean status, TablePage<T> result){
		this(status);
		this.result = result;
	}

	public TablePage<T> getResult() {
		return result;
	}

	public void setResult(TablePage<T> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "TablePageReponse [result=" + result + "]";
	}
	
}
