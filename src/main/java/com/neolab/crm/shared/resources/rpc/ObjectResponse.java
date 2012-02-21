package com.neolab.crm.shared.resources.rpc;

@SuppressWarnings("serial")
public class ObjectResponse<P> extends Response{

	private P object;
	
	public ObjectResponse() {
	}

	public ObjectResponse(boolean status, P object) {
		super(status);
		this.object = object;
	}

	public P getObject() {
		return object;
	}

	public void setObject(P object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "ObjectResponse [object=" + object + "]";
	}
	
	
}
