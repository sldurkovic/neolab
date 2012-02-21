package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

@Repository
public class UsersDAOImpl implements UsersDAO{

	private Log log = LogFactory.getLog(getClass().getName());
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public User getUser(int uid) {
		return (User) hibernateTemplate.get(User.class, uid);
	}

	@Override
	public ArrayList<User> getUsers(int offset, int limit, ColumnSort sort) {
		DetachedCriteria filter = DetachedCriteria.forClass(User.class);
		if(sort.isAscending())
			filter.addOrder(Order.asc(sort.getColumn()));
		else
			filter.addOrder(Order.desc(sort.getColumn()));
		List<User> list = hibernateTemplate.findByCriteria(filter,offset,limit);
		for(User u : list){
			log.debug(u);
		}
		return (ArrayList<User>)list;
	}

	@Override
	public int getUsersCount() {
		return DataAccessUtils.intResult(hibernateTemplate.find("select count(*) from User"));
	}

	@Override
	public boolean registerUser(String name, String email, int level, String password) {
		log.debug("Registering user: "+email);
		User user = new User();
		user.setEmail(email);
		user.setLevel(level);
		List l = hibernateTemplate.findByExample(user);
		if(l != null && l.size()>0)
			return false;
		user.setFirstName(name);
		user.setPassword(password);
		user.setLastName("");
		user.setLevel(3);
		user.setPhone("");
		user.setSite("");
		user.setEmailHost("");
		user.setEmailPassword("");
		hibernateTemplate.save(user);
		return true;
	}

	@Override
	public User login(String username, String password) {
		log.debug("Logging in: "+username);
		User user = new User();
		user.setEmail(username);
		user.setPassword(password);
		List l = hibernateTemplate.findByExample(user);
		if(l.size() > 0)
			user = (User)l.get(0);
		else
			user = null;
//		System.out.println(user);
		return user;
	}

	@Override
	public boolean update(User user) {
		try{
			User temp = getUser(user.getUid());
			temp.merge(user);
			log.debug(temp);
			hibernateTemplate.update(temp);
		}catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<User> getUsersById(ArrayList<Integer> uids, int offset, int limit,
			ColumnSort sort) {
		if(uids.size() == 0)
			return new ArrayList<User>();
		DetachedCriteria filter = DetachedCriteria.forClass(User.class);
		filter.add(Restrictions.in("uid", uids));
		if(sort.isAscending())
			filter.addOrder(Order.asc(sort.getColumn()));
		else
			filter.addOrder(Order.desc(sort.getColumn()));
		List<User> list = hibernateTemplate.findByCriteria(filter,offset,limit);
		for(User u : list){
			log.debug(u);
		}
		return (ArrayList<User>)list;
	}

}
