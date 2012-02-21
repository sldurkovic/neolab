package com.neolab.crm.server.persistance.dao;

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
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

@Repository
public class TaskActivityDAOImpl implements TaskActivityDAO {

	private Log log = LogFactory.getLog(getClass().getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public void assignTask(int tid, ArrayList<Integer> uids) {
		for (Integer uid : uids) {
			TaskActivity ta = new TaskActivity();
			ta.setTid(tid);
			ta.setUid(uid);
			hibernateTemplate.merge(ta);
		}
	}

	@Override
	public void removeAssignedTasks(ArrayList<Integer> tids, int uid) {
		for (Integer tid : tids) {
			TaskActivity ta = new TaskActivity();
			ta.setTid(tid);
			ta.setUid(uid);
			hibernateTemplate
					.delete(hibernateTemplate.findByExample(ta).get(0));
		}
	}

	@Override
	public void assignTasks(ArrayList<Integer> tids, int uid) {
		for (Integer tid : tids) {
			TaskActivity ta = new TaskActivity();
			ta.setTid(tid);
			ta.setUid(uid);
			hibernateTemplate.saveOrUpdate(ta);
		}
	}

	@Override
	public int getUsersCountForTask(final int tid, boolean reverse) {
		if(reverse){
//			return DataAccessUtils.intResult(hibernateTemplate
//					.find("select distinct count(*) from TaskActivity where tid!="
//							+ tid));
			return hibernateTemplate.execute(new HibernateCallback<Integer>() {@Override
				public Integer doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createSQLQuery("SELECT COUNT(*) FROM users JOIN (SELECT * FROM users WHERE uid NOT IN (SELECT uid FROM users JOIN task_activity USING (uid) WHERE task_activity.tid = "+tid+") AND uid  IN (SELECT uid FROM project_activity JOIN tasks USING (pid) WHERE tid="+tid+" ) GROUP BY uid) AS v USING (uid)");
					return DataAccessUtils.intResult(query.list());
				}
				});
		}return DataAccessUtils.intResult(hibernateTemplate
				.find("select distinct count(*) from TaskActivity where tid="
						+ tid));
	}

	@Override
	public ArrayList<User> getUsersForTask(final int start, final int length,
			final ColumnSort sort, final int tid, final boolean reverse) {
		return hibernateTemplate
				.execute(new HibernateCallback<ArrayList<User>>() {
					@Override
					public ArrayList<User> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = null;
						if(!reverse){
						query = session
								.createSQLQuery(
										"SELECT DISTINCT * from task_activity left join users USING (uid) WHERE task_activity.tid="
												+ tid
												+ " "
												+ sort.toSqlString())
								.addEntity(User.class).setFirstResult(start)
								.setMaxResults(length);
						}else{
							query = session
							.createSQLQuery(
									"SELECT * FROM users WHERE uid NOT IN (SELECT uid FROM users JOIN task_activity USING (uid) WHERE task_activity.tid = "+tid+") AND uid  IN (SELECT uid FROM project_activity JOIN tasks USING (pid) WHERE tid="+tid+" ) GROUP BY uid"
											+ " "
											+ sort.toSqlString())
							.addEntity(User.class).setFirstResult(start)
							.setMaxResults(length);
						}
						ArrayList<User> users = (ArrayList<User>) query.list();
						return users;
					}
				});
	}

	@Override
	public void removeTasksActivity(ArrayList<Task> tasks,
			ArrayList<Integer> uids) {
		if(tasks.size() == 0)
			return;
		DetachedCriteria filter = DetachedCriteria.forClass(TaskActivity.class);
		ArrayList<Integer> tasksInts = new ArrayList<Integer>();
		for (Task t : tasks)
			tasksInts.add(t.getTid());
		filter.add(Restrictions.in("tid", tasksInts));
		if(uids.size() == 1)
			filter.add(Restrictions.eq("uid", uids.get(0)));
		else
			filter.add(Restrictions.in("uid", uids));
		List<TaskActivity> list = hibernateTemplate.findByCriteria(filter);
		for (TaskActivity taskActivity : list) {
			hibernateTemplate.delete(taskActivity);
		}
	}

	@Override
	public int getTaskCountByDate(int uid, int pid, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		List list = hibernateTemplate
				.find("select distinct count(*) from TaskActivity as ta join ta.task as task where day(task.dateStart)="
						+ cal.get(Calendar.DAY_OF_MONTH)
						+ "AND month(task.dateStart)="
						+ (cal.get(Calendar.MONTH) + 1)
						+ "AND year(task.dateStart)="
						+ cal.get(Calendar.YEAR)
						+ " AND ta.uid=" + uid + ((pid != -1)? " AND task.pid="+pid :""));
		return DataAccessUtils.intResult(list);
	}

	@Override
	public ArrayList<Task> getTasksByDate(final int start, final int length,
			final ColumnSort sort, final int uid, final int pid, Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return hibernateTemplate
				.execute(new HibernateCallback<ArrayList<Task>>() {
					@Override
					public ArrayList<Task> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(
										"select distinct * from task_activity as ta join tasks as task using(tid) where day(task.date_start)="
										+ cal.get(Calendar.DAY_OF_MONTH)
										+ " AND month(task.date_start)="
										+ (cal.get(Calendar.MONTH) + 1)
										+ " AND year(task.date_start)="
										+ cal.get(Calendar.YEAR)
										+ " AND ta.uid=" + uid
										+ " AND ta.uid=" + uid + ((pid != -1)? " AND task.pid="+pid :"")
										+" "
										+ sort.toSqlString())
								.addEntity(Task.class)
								.setFirstResult(start)
								.setMaxResults(length);
						// query =
						// session.createSQLQuery("SELECT DISTINCT * from users left join task_activity as ta USING (uid) left join tasks USING(tid) WHERE tasks.pid="+pid+" AND uid!="+uid+" AND tid NOT IN (SELECT tid FROM task_activity WHERE uid = "+uid+")"+sort.toSqlString()).addEntity(Task.class).setFirstResult(start).setMaxResults(length);
						ArrayList<Task> tasks = (ArrayList<Task>) query.list();
						// System.out.println(tasks);
						return tasks;
					}
				});
	}

}
