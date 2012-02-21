package com.neolab.crm.shared.resources;

public interface AsyncProvider<T> {
	void getObject(int id, Requires<T> caller);
}
