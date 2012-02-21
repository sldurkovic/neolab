package com.neolab.crm.shared.resources.rpc;

import java.io.Serializable;

import com.neolab.crm.shared.domain.User;

@SuppressWarnings("serial")
public class Response implements Serializable{
	
	protected boolean status;
	protected String msg;
	
	public Response(){
	}
	
	public Response(boolean status) {
		this.status = status;
	}
	
	public Response(boolean status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", msg=" + msg + "]";
	}

}
