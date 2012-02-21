package com.neolab.crm.server.services;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtwidgets.server.spring.GWTRequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.neolab.crm.server.persistance.Database;
import com.neolab.crm.server.resources.timeline.XMLEvents;
import com.neolab.crm.server.resources.timeline.XMLEvents.Event;
import com.neolab.crm.shared.domain.Category;
import com.neolab.crm.shared.domain.News;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.Task;
import com.neolab.crm.shared.domain.TaskStatus;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;
import com.neolab.crm.shared.resources.Configuration;
import com.neolab.crm.shared.resources.ProjectPage;
import com.neolab.crm.shared.resources.TablePage;
import com.neolab.crm.shared.resources.rpc.ObjectResponse;
import com.neolab.crm.shared.resources.rpc.Response;
import com.neolab.crm.shared.resources.rpc.TablePageReponse;
import com.neolab.crm.shared.resources.rpc.UserResponse;
import com.neolab.crm.shared.services.NeoService;

@Service
@GWTRequestMapping("/neolab/neo.rpc")
public class NeoServiceImpl implements NeoService {

	private static Log log = LogFactory.getLog(NeoServiceImpl.class);

	private static HashMap<String, User> userMapping = new HashMap<String, User>();

	// @Autowired
	// private ValidatorFactory validator;

	@Autowired
	private Database DB;

	@Autowired(required = false)
	private Configuration configuration;

	@Autowired
	private JavaMailSenderImpl mailSender;

	public NeoServiceImpl() {
	}

	@Override
	public UserResponse login(String username, String password) {
		log.debug("[Logging in]: " + username);
		User u = DB.login(username, password);
		if (u != null) {
			String id = RequestContextHolder.currentRequestAttributes()
					.getSessionId();
			RequestContextHolder.currentRequestAttributes().setAttribute(
					"user", u, RequestAttributes.SCOPE_GLOBAL_SESSION);
			synchronized (userMapping) {
				userMapping.put(id, u);
			}
			return new UserResponse(true, u);
		} else
			return new UserResponse(false, "Invalid username and/or password");
	}

	@Override
	public Response registerUser(String name, String email, int level) {
		log.debug("[Processing mail form]");
		String password = UUID.randomUUID().toString().substring(0, 8);
		try {
			boolean success = DB.registerUser(name, email, level, password);
			if (!success)
				return new Response(false, "Registration failed");
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject("Neolab");
			msg.setText("Postovani,\nPozvani ste da se prikljucite Neolab timu. \n\nVase korisnicko ime: "
					+ email
					+ "\nVasa sifra: "
					+ password
					+ "\n\nS postovanjem, \nNeolab");
			mailSender.send(msg);
		} catch (Exception e) {
			log.error("sendMail error:  " + e.getMessage());
		}
		return new Response(true, "All went ok");
	}

	@Override
	public TablePage<User> requestUsersTable(int offset, int limit,
			ColumnSort sort) {
		log.debug("[Handling user table request]");
		TablePage<User> page = new TablePage<User>();
		if (sort == null)
			sort = new ColumnSort(ColumnSort.FIRST_NAME, true);
		page.setTotal(DB.getUsersCount());
		page.setList(DB.getUsers(offset, limit, sort));
		return page;
	}

	@Override
	public TablePage<Project> requestProjectsTable(int offset, int limit,
			ColumnSort sort, int cid) {
		log.debug("[Handling projects table request]");
		TablePage<Project> page = new TablePage<Project>();
		if (sort == null)
			sort = new ColumnSort(ColumnSort.TITLE, true);
		page.setTotal(DB.getProjectsCount(cid));
		page.setList(DB.getProjects(offset, limit, sort, cid));
		return page;
	}

	@Override
	public TablePage<Task> requestTasksTable(int start, int length,
			ColumnSort sort, int pid) {
		log.debug("[Handling tasks table request]");
		TablePage<Task> page = new TablePage<Task>();
		if (sort == null)
			sort = new ColumnSort(ColumnSort.TITLE, true);
		page.setTotal(DB.getTasksCount(pid));
		page.setList(DB.getTasks(start, length, sort, pid));
		return page;
	}

	public User getActiveUser() {
		User user = (User) RequestContextHolder.currentRequestAttributes()
				.getAttribute("user", RequestAttributes.SCOPE_GLOBAL_SESSION);
		if (user != null) {
			user = DB.getUser(user.getUid());
			RequestContextHolder.currentRequestAttributes().setAttribute(
					"user", user, RequestAttributes.SCOPE_GLOBAL_SESSION);
		}

		log.debug("[USER]: " + user);
		return user;
	}

	@Override
	public Response updateUser(User user) {
		log.debug("[Updating user]: " + user);
		if (user == null) {
			return new Response(false, "User is null");
		} else {
			boolean success = DB.updateUser(user);
			if (success)
				return new Response(true, "User updated successfully");
			return new Response(false, "Update failed, something went wrong");
		}

	}

	@Override
	public Response updateUsers(ArrayList<User> list) {
		log.debug("[Updating user list] ");
		if (list == null) {
			return new Response(false, "User is null");
		} else {
			for (User user : list) {
				DB.updateUser(user);
			}
			return new Response(true, "User(s) updated successfully");
		}

	}

	@Override
	public Response logout() {
		log.debug("Logging out...");
		// RequestContextHolder.resetRequestAttributes();
		RequestContextHolder.currentRequestAttributes().removeAttribute("user",
				RequestAttributes.SCOPE_GLOBAL_SESSION);
		return new Response(true);
	}

	@Override
	public Response sendMail(String to, int uidFrom, String subject, String body) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			User user = DB.getUser(uidFrom);
			log.debug(user);
			msg.setTo(to);
			msg.setSubject(subject);
			msg.setText(body);
			if (!user.getEmailHost().isEmpty()
					&& !user.getEmailProtocol().isEmpty()
					&& !user.getEmailPassword().isEmpty()
					&& user.getEmailPort() != 0) {
				log.debug("custom mail options");
				mailSender.setUsername(user.getEmail());
				mailSender.setProtocol(user.getEmailProtocol());
				mailSender.setHost(user.getEmailHost());
				mailSender.setPort(user.getEmailPort());
			}
			mailSender.send(msg);
			return new Response(true, "Email sent");
		} catch (Exception e) {
			log.error("sendMail error:  " + e.getMessage());
		}
		return new Response(false, "Send mail failed");
	}

	@Override
	public ObjectResponse<ArrayList<Project>> getUserCategoryProjects(int uid,
			int cat) {
		log.debug("[getUserCategoryProjects]: uid=" + uid + " cat=" + cat);
		ObjectResponse<ArrayList<Project>> response = new ObjectResponse<ArrayList<Project>>();
		response.setStatus(true);
		response.setObject(DB.getUserCategoryProjects(uid, cat));
		log.debug("[getUserCategoryProjects] RESPONSE: " + response);
		return response;
	}

	@Override
	public ObjectResponse<Integer> createProject(String title,
			String description, int cid) {
		return new ObjectResponse<Integer>(true, new Integer(DB.createProject(
				1, title, description, cid)));
	}

	@Override
	public ObjectResponse<ProjectPage> getProjectById(int pid) {
		log.debug("[getting project by id]");
		ProjectPage page = new ProjectPage();

		// get project
		Project p = DB.getProjectById(pid);
		if (p == null)
			log.debug("project is null");
		page.setProject(p);

		// get documents
		File file = new File(configuration.getProjectsPath());
		if (file.listFiles().length > 0) {
			for (int i = 0; i < file.listFiles().length; i++) {
				File f = file.listFiles()[i];
				if (f.getName().startsWith(p.getPid() + "-")) {

					if (f.listFiles().length > 0) {
						for (int j = 0; j < f.listFiles().length; j++) {
							File e = f.listFiles()[j];
							if (!e.getName().startsWith("."))
								page.addDocument(e.getName());
						}
					}
					break;
				}
			}
		}
		
		page.setMember(DB.isProjectMember(getActiveUser().getUid(), pid));
		log.debug("response page: "+page);

		return new ObjectResponse<ProjectPage>(true, page);
	}

	@Override
	public TablePage<User> requestUsersCellList(int pid, int start, int length) {
		log.debug("[Handling user cell list request]");
		TablePage<User> page = new TablePage<User>();
		ColumnSort sort = new ColumnSort(ColumnSort.FIRST_NAME, true);
		page.setTotal(DB.getUsersOnProjectCount(pid));
		page.setList(DB.getUsersOnProject(pid, start, length, sort));
		return page;
	}

	@Override
	public Response addActivity(int pid, ArrayList<Integer> uids) {
		log.debug("[adding activity]");
		DB.addActivity(pid, uids);
		return new Response(true);
	}

	@Override
	public Response removeActivity(int pid, ArrayList<Integer> uids) {
		log.debug("[removing activity]");
		DB.removeActivity(pid, uids);
		return new Response(true);
	}

	@Override
	public Response addNewTask(int pid, String title, String description,
			Date start, Date end) {
		log.debug("[Adding new task]");
		DB.addNewTask(pid, title, description, start, end);
		Project p = DB.getProjectById(pid);
		DB.createNews(getActiveUser().getUid(), "New task! "+p.getTitle()+":: "+title, description, 3, pid);
		return new Response(true);
	}

	public void deleteTask(int i) {
		log.debug("[Deleting task]");
		DB.deleteTask(i);
	}

	public Response assignTask(int tid, ArrayList<Integer> uids) {
		log.debug("[Assigning task]");
		DB.assignTask(tid, uids);
		return new Response(true, "Task assigned!");
	}

	public Response assignTasks(ArrayList<Integer> tids, int uid) {
		log.debug("[Assigning tasks]");
		DB.assignTasks(tids, uid);
		return new Response(true, "Task(s) assigned!");
	}

	public void removeAssignedTasks(ArrayList<Integer> tids, int uid) {
		log.debug("[Removing task]");
		DB.removeAssignedTasks(tids, uid);
	}

	@Override
	public Response changeStatus(int tid, TaskStatus status) {
		DB.changeStatus(tid, status);
		Task t = DB.getTaskById(tid);
		DB.createNews(getActiveUser().getUid(), t.getTitle(), "Task status updated to: "+status, 3, 0);
		return new Response(true, "Task status changed");
	}

	@Override
	public Response updateProject(Project project) {
		log.debug("[Updating project]");
		DB.updateProject(project);
		return new Response(true, "Project updated!");
	}

	@Override
	public Response deleteProject(int pid) {
		log.debug("[Deleting project and related tasks]");
		DB.deleteProject(pid);
		return new Response(true, "Project deleted!");
	}

	@Override
	public ObjectResponse<ArrayList<Project>> getUserProjects(int uid) {
		log.debug("[Retrieving projects]");
		ArrayList<Project> list = DB.getUserProjects(uid);
		return new ObjectResponse<ArrayList<Project>>(true, list);
	}

	@Override
	public TablePage<Task> getUserTasksTable(int start, int length,
			ColumnSort sort, int pid, int uid, boolean reverse) {
		log.debug("[Handling tasks table request]");
		TablePage<Task> page = new TablePage<Task>();
		if (sort == null)
			sort = new ColumnSort(ColumnSort.TITLE, true);
		page.setTotal(DB.getUserTasksCount(pid, uid, reverse));
		page.setList(DB.getUserTasks(start, length, sort, pid, uid, reverse));
		return page;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public TablePage<User> requestUsersTableByTask(int start, int length,
			ColumnSort sort, int tid, boolean reverse) {
		log.debug("[Handling user table request for task]");
		TablePage<User> page = new TablePage<User>();
		if (sort == null)
			sort = new ColumnSort(ColumnSort.FIRST_NAME, true);
		page.setTotal(DB.getUsersCountForTask(tid, reverse));
		page.setList(DB.getUsersForTask(start, length, sort, tid, reverse));
		return page;
	}

	@Override
	public TablePage<Task> requestTasksTableByDate(int start, int length,
			ColumnSort sort, int uid, int pid, Date date) {
		if (date == null) {
			if (uid > 0) {
				return getUserTasksTable(start, length, sort, pid, uid, false);
			} else {
				return requestTasksTable(start, length, sort, pid);
			}
		}
		log.debug("[Handling tasks table request]");
		TablePage<Task> page = new TablePage<Task>();
		if (sort == null)
			sort = new ColumnSort(ColumnSort.TITLE, true);
		page.setTotal(DB.getTasksCountByDate(uid, pid, date));
		page.setList(DB.getTasksByDate(start, length, sort, uid, pid, date));
		return page;
	}

	@Override
	public String constructTimelineXml(int uid) {
		ArrayList<Task> tasks = getUserTasksTable(0, Integer.MAX_VALUE, null,
				-1, uid, false).getList();
		XMLEvents events = new XMLEvents();
		if (tasks.size() > 0) {
			for (Task task : tasks) {
				try {
					Date date = task.getDateStart();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf2 = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String d = sdf.format(sdf2.parse(date.toString()));
					Event event = events.new Event(d, d);
					event.setIsDurations(false);
					event.setIcon("/site/image/green-circle.png");
					event.setTitle(task.getTitle());
					event.setImage("/site/image/Timeline_Logo_Thumb.png");
					event.setContent(task.getDescription());
					events.addEvent(event);
				} catch (Exception e) {
					log.debug("Timeline xml parsing error");
					e.printStackTrace();
				}
			}
		}
		return events.getXml();
	}

	@Override
	public TablePageReponse<News> getNews(int level, int pid, int uid,
			int start, int end) {
		log.debug("[Handling news request]: level=" + level + " uid=" + uid
				+ " start=" + start + " end=" + end);
		TablePageReponse<News> tpr = new TablePageReponse<News>(true);

		ArrayList<Integer> pids = new ArrayList<Integer>();
		if (pid == 0) {
			ArrayList<Project> pas = DB.getUserProjects(uid);
			if (pas.size() > 0) {
				for (Project pa : pas) {
					pids.add(pa.getPid());
				}
			}
			tpr.setResult(new TablePage<News>(DB.getNewsCount(level, pids), DB
					.getNews(level, pids, start, end)));
		} else {
			tpr.setResult(new TablePage<News>(DB.getNewsForProjectCount(level,
					pid), DB.getNewsForProject(level, pid, start, end)));
		}

		return tpr;
	}

	@Override
	public Response createNews(int uid, String title, String body, int level,
			int pid) {
		DB.createNews(uid, title, body, level, pid);
		return new Response(true, "News posted!");
	}
	
	@Override
	public Response createCategory(String title){
		DB.createCategory(title);
		return new Response(true,"New category created");
	}
	
	@Override
	public ArrayList<Category> getCategories(){
		return DB.getCategories();
	}

}
