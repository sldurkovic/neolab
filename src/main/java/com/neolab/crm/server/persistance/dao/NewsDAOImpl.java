package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import com.neolab.crm.shared.domain.News;

@Repository
public class NewsDAOImpl implements NewsDAO {

	private Log log = LogFactory.getLog(getClass().getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public ArrayList<News> getNews(int level, ArrayList<Integer> pids, int offset, int limit) {
		DetachedCriteria filter = DetachedCriteria.forClass(News.class);
		if(pids.size()>0){
			pids.add(0);
			filter.add(Restrictions.in("pid", pids));
		}
		filter.createCriteria("user");
		filter.addOrder(Order.desc("date"));
		if(level != 0)
			filter.add(Restrictions.between("level", 0, level));
		
		List<News> list = hibernateTemplate.findByCriteria(filter,offset,limit);
		if(list.size()==0)
			return new ArrayList<News>();
		for(News u : list){
			log.debug(u);
		}
		return (ArrayList<News>)list;
	}

	@Override
	public int getNewsCount(int level, ArrayList<Integer> pids) {
		DetachedCriteria filter = DetachedCriteria.forClass(News.class);
		pids.add(0);
		filter.add(Restrictions.in("pid", pids));
		filter.add(Restrictions.between("level", 0, level));
		filter.setProjection(Projections.rowCount());
		return DataAccessUtils.intResult(hibernateTemplate.findByCriteria(filter));
	}

	@Override
	public void createNews(int uid, String title, String body, int level,
			int pid) {
		News news  = new News();
		news.setUid(uid);
		if(title == null)
			title = "";
		news.setTitle(title);
		news.setLevel(level);
		news.setPid(pid);
		news.setBody(body);
		news.setDate(new Date());
		hibernateTemplate.save(news);
	}

	@Override
	public ArrayList<News> getNewsForProject(int level, int pid, int start,
			int end) {
		DetachedCriteria filter = DetachedCriteria.forClass(News.class);
		filter.add(Restrictions.eq("pid", pid));
		filter.createCriteria("user");
		filter.addOrder(Order.desc("date"));
		if(level != 0)
			filter.add(Restrictions.between("level", 0, level));
		
		List<News> list = hibernateTemplate.findByCriteria(filter,start,end);
		if(list.size()==0)
			return new ArrayList<News>();
		for(News u : list){
			log.debug(u);
		}
		return (ArrayList<News>)list;
	}

	@Override
	public int getNewsForProjectCount(int level, int pid) {
		DetachedCriteria filter = DetachedCriteria.forClass(News.class);
		filter.add(Restrictions.eq("pid", pid));
		filter.add(Restrictions.between("level", 0, level));
		filter.setProjection(Projections.rowCount());
		return DataAccessUtils.intResult(hibernateTemplate.findByCriteria(filter));
	}

}
