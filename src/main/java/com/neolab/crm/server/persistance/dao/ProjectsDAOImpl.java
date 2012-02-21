package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import java.util.Date;
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

import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.ProjectActivity;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

@Repository
public class ProjectsDAOImpl implements ProjectsDAO{

	private Log log = LogFactory.getLog(getClass().getName());


	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public int getProjectCount(int cid) {
		return DataAccessUtils.intResult(hibernateTemplate.find("select count(*) from Project WHERE cid="+cid));
	}

	@Override
	public ArrayList<Project> getProjects(int offset, int limit, ColumnSort sort, int cid) {
		DetachedCriteria filter = DetachedCriteria.forClass(Project.class);
		filter.add(Restrictions.eq("cid", cid));
		if(sort.isAscending())
			filter.addOrder(Order.asc(sort.getColumn()));
		else
			filter.addOrder(Order.desc(sort.getColumn()));
		List<Project> list = hibernateTemplate.findByCriteria(filter,offset,limit);
		return (ArrayList<Project>)list;
	}

	@Override
	public ArrayList<Project> getProjectsByActivity(ArrayList<ProjectActivity> result, int cat) {
		DetachedCriteria filter = DetachedCriteria.forClass(Project.class);
		ArrayList<Integer> in = new ArrayList<Integer>();
		for (ProjectActivity pa : result) {
			in.add(pa.getPid());
		}
		if(cat != -1)
			filter.add(Restrictions.eq("cid", cat));
		filter.add(Restrictions.in("pid", in));
		List<Project> list = hibernateTemplate.findByCriteria(filter);
		return (ArrayList<Project>)list;
	}

	@Override
	public int createProject(int uid, String title, String description, int cid) {
		Project p = new Project();
		p.setCid(cid);
		p.setCreatedBy(uid);
		p.setDateCreated(new Date());
		p.setTitle(title);
		p.setDescription(description);
		hibernateTemplate.save(p);
		return DataAccessUtils.intResult(hibernateTemplate.find("select max(pid) from Project"));
	}

	@Override
	public Project getProject(int pid) {
		return (Project) hibernateTemplate.get(Project.class, pid);
	}

	@Override
	public void updateProject(Project project) {
		hibernateTemplate.update(project);
	}

	@Override
	public void deleteProject(int pid) {
		Project project = (Project) hibernateTemplate.get(Project.class, pid);
		hibernateTemplate.delete(project);
	}
}
