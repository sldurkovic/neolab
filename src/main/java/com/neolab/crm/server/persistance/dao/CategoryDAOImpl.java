package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.neolab.crm.shared.domain.Category;

@Repository
public class CategoryDAOImpl implements CategoryDAO{

	private Log log = LogFactory.getLog(getClass().getName());
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public void createCategory(String title) {
		Category c = new Category();
		c.setTitle(title);
		hibernateTemplate.save(c);
	}

	@Override
	public ArrayList<Category> getCategories() {
		List<Category> list = hibernateTemplate.loadAll(Category.class);
		return (ArrayList<Category>) list;
	}

}
