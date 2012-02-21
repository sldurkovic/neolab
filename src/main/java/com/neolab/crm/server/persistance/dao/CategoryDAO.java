package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;

import com.neolab.crm.shared.domain.Category;

public interface CategoryDAO {
	
	void createCategory(String title);
	
	ArrayList<Category> getCategories();
	
}
