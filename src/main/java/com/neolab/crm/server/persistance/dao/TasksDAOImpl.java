package com.neolab.crm.server.persistance.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.TaskActivity;
import com.neolab.crm.shared.domain.TaskStatus;
import com.neolab.crm.shared.resources.ColumnSort;

@Repository
public class TasksDAOImpl implements TasksDAO{

	private Log log = LogFactory.getLog(getClass().getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;


	@Override
	public int getTaskCount(int pid) {
		if(pid != -1)
			return DataAccessUtils.intResult(hibernateTemplate.find("select count(*) from Task WHERE pid="+pid));
		else
			return DataAccessUtils.intResult(hibernateTemplate.find("select count(*) from Task "));
	}


	@Override
	public ArrayList<Task> getTasks(int start, int length, ColumnSort sort,
			int pid) {
		DetachedCriteria filter = DetachedCriteria.forClass(Task.class);
		if(pid != -1)
		filter.add(Restrictions.eq("pid", pid));
		if(sort.isAscending())
			filter.addOrder(Order.asc(sort.getColumn()));
		else
			filter.addOrder(Order.desc(sort.getColumn()));
		List<Task> list = hibernateTemplate.findByCriteria(filter,start,length);
		for(Task u : list){
			log.debug(u);
		}
		return (ArrayList<Task>)list;
	}


	@Override
	public void addTask(int pid, String title, String description, Date start,
			Date end) {
		Task task = new Task();
		task.setTitle(title);
		task.setPid(pid);
		task.setDescription(description);
		task.setDateStart(start);
		task.setDateEnd(end);
		task.setStatus(TaskStatus.ACTIVE.toString());
		hibernateTemplate.saveOrUpdate(task);
	}


	@Override
	public void deleteTask(int tid) {
		hibernateTemplate.delete(hibernateTemplate.get(Task.class, tid));
	}
	

	@Override
	public void changeStatus(int tid, TaskStatus status) {
		log.debug("[changing status]: "+tid+" status="+status);
		Task temp = (Task) hibernateTemplate.get(Task.class, tid);
		temp.setStatus(status.toString());
		hibernateTemplate.update(temp);
	}


	@Override
	public int getUserTaskCount(final int pid, final int uid, final boolean reverse) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {@Override
		public Integer doInHibernate(Session session)
				throws HibernateException, SQLException {
			Query query;
			if(!reverse){
				if(pid != -1)
				query= session.createQuery("select count(*) from TaskActivity as ta join ta.task where pid="+pid+" AND uid="+uid);
				else
				query= session.createQuery("select count(*) from TaskActivity as ta join ta.task where uid="+uid);
			}else
//				query= session.createQuery("select count(*) from TaskActivity as ta right join ta.task where pid="+pid+" AND uid!="+uid+" AND ta.task.tid NOT IN (SELECT tid FROM TaskActivity WHERE uid = "+uid+")");
				query= session.createSQLQuery("select distinct count(*)  from tasks left join (SELECT DISTINCT * FROM task_activity WHERE uid != "+uid+" GROUP BY tid) AS t USING (tid) where tasks.pid="+pid+" AND tasks.tid NOT IN (SELECT tid FROM task_activity WHERE uid = "+uid+")");

			return DataAccessUtils.intResult(query.list());
		}
		});
 
	}


	@Override
	public ArrayList<Task> getUserTasks(final int start, final int length, final ColumnSort sort,
			final int pid, final int uid, final boolean reverse) {
		
		return hibernateTemplate.execute(new HibernateCallback<ArrayList<Task>>() {@Override
			public ArrayList<Task> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query;
				if(!reverse){
					if(pid != -1)
						query = session.createSQLQuery("SELECT DISTINCT * from users left join task_activity as ta USING (uid) join tasks USING(tid) WHERE tasks.pid="+pid+" AND uid="+uid+" "+sort.toSqlString()).addEntity(Task.class).setFirstResult(start).setMaxResults(length);
					else
						query = session.createSQLQuery("SELECT DISTINCT * from users left join task_activity as ta USING (uid) join tasks USING(tid) WHERE uid="+uid+" "+sort.toSqlString()).addEntity(Task.class).setFirstResult(start).setMaxResults(length);
				}else{
					query= session.createSQLQuery("select distinct *  from tasks left join (SELECT DISTINCT * FROM task_activity WHERE uid != "+uid+" GROUP BY tid) AS t USING (tid) where tasks.pid="+pid+" AND tasks.tid NOT IN (SELECT tid FROM task_activity WHERE uid = "+uid+")"+sort.toSqlString()).addEntity(Task.class).setFirstResult(start).setMaxResults(length);
//					query = session.createSQLQuery("SELECT DISTINCT * from users left join task_activity as ta USING (uid) left join tasks USING(tid) WHERE tasks.pid="+pid+" AND uid!="+uid+" AND tid NOT IN (SELECT tid FROM task_activity WHERE uid = "+uid+")"+sort.toSqlString()).addEntity(Task.class).setFirstResult(start).setMaxResults(length);
				}
				ArrayList<Task> tasks = (ArrayList<Task>) query.list();
//				System.out.println(tasks);
				return tasks;
			}
			});
	}


	@Override
	public ArrayList<Task> getTasksForProject(int pid) {
		DetachedCriteria filter = DetachedCriteria.forClass(Task.class);
		filter.add(Restrictions.eq("pid", pid));
		List<Task> list = hibernateTemplate.findByCriteria(filter);
		return (ArrayList<Task>)list;
	}


	@Override
	public int getTaskCountByDate(int pid, Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
			return DataAccessUtils.intResult(hibernateTemplate.find("select count(*) from Task WHERE pid="+pid+" AND " +
					"day(date_start)="
										+ cal.get(Calendar.DAY_OF_MONTH)
										+ " AND month(date_start)="
										+ (cal.get(Calendar.MONTH) + 1)
										+ " AND year(date_start)="
										+ cal.get(Calendar.YEAR)
										));

	}


	@Override
	public ArrayList<Task> getTasksByDate(int start, int length,
			ColumnSort sort, int uid, int pid, Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DetachedCriteria filter = DetachedCriteria.forClass(Task.class);
		if(pid != -1)
		filter.add(Restrictions.eq("pid", pid));
		filter.add(Restrictions.sqlRestriction("day(date_start) ="+ cal.get(Calendar.DAY_OF_MONTH)));
		filter.add(Restrictions.sqlRestriction("month(date_start) ="+ (cal.get(Calendar.MONTH)+1)));
		filter.add(Restrictions.sqlRestriction("year(date_start) ="+ cal.get(Calendar.YEAR)));

		if(sort.isAscending())
			filter.addOrder(Order.asc(sort.getColumn()));
		else
			filter.addOrder(Order.desc(sort.getColumn()));
		List<Task> list = hibernateTemplate.findByCriteria(filter,start,length);
		for(Task u : list){
			log.debug(u);
		}
		return (ArrayList<Task>)list;

	}


	@Override
	public ArrayList<Task> getTasksByDate(int pid, Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DetachedCriteria filter = DetachedCriteria.forClass(Task.class);
		if(pid != -1)
		filter.add(Restrictions.eq("pid", pid));
		filter.add(Restrictions.sqlRestriction("day(date_start) ="+ cal.get(Calendar.DAY_OF_MONTH)));
		filter.add(Restrictions.sqlRestriction("month(date_start) ="+ (cal.get(Calendar.MONTH)+1)));
		filter.add(Restrictions.sqlRestriction("year(date_start) ="+ cal.get(Calendar.YEAR)));

		List<Task> list = hibernateTemplate.findByCriteria(filter);
		return (ArrayList<Task>)list;
	}


	@Override
	public Task getTaskById(int tid) {
		return hibernateTemplate.get(Task.class, tid);
	}

}
