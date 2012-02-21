package com.neolab.crm.shared.resources.rpc;

import com.neolab.crm.shared.domain.User;

@SuppressWarnings("serial")
public class UserResponse extends Response{

	private User user;

	public UserResponse() {
	}
	
	public UserResponse(boolean status, String msg) {
		super(status, msg);
	}
	
	public UserResponse(boolean status, User user) {
		super(status);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
