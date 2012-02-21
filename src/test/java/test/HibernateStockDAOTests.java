package test;

import java.util.ArrayList;
import java.util.Date;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.neolab.crm.server.persistance.Database;
import com.neolab.crm.server.persistance.dao.TasksDAO;
import com.neolab.crm.server.persistance.dao.UsersDAO;
import com.neolab.crm.server.services.NeoServiceImpl;
import com.neolab.crm.shared.domain.TaskStatus;

@RunWith(SpringJUnit4ClassRunner.class)
//specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations={"classpath:applicationContext.xml", "classpath:spring/spring.xml"})

public class HibernateStockDAOTests {
//	// this instance will be dependency injected by type
    @Autowired    
    private UsersDAO usersDao;
    
    @Autowired
    private TasksDAO tasksDao;
    
    @Autowired
    Database DB;
//    
    @Autowired
    NeoServiceImpl test;
    
//    @Test
//    public void retrieve() throws Exception {
//    	assertNotNull(usersDao);
//
//    }
//    No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet/DispatcherPortlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.

    @org.junit.Test
    public void test() throws Exception{
//    	User u = usersDao.getUser(1);
//    	System.out.println(u.getLastName());
//    	DB.getUsers(0,10,new ColumnSort(ColumnSort.LAST_NAME, true));
//    	System.out.println(DB.getUsersCount());
//    	boolean success = test.registerUser("Slobodan", "asdasdasdfgertrgdf@gmail.com");
//    	System.out.println(success);
//    	System.out.println(test.login("slobodan@gmail.com","1234"));
//    	User user = new User();
//    	user.setUid(1);
//    	user.setPassword("1234");
//    	test.updateUser(user);
//    	System.out.println(usersDao.getUser(1));
//    	System.out.println(test.requestProjectsTable(0, 0, new ColumnSort(ColumnSort.DATE_CREATED, true),1));
//    	System.out.println(tasksDao.getTasks(0, 10, new ColumnSort("title", true), 1));
//    	System.out.println(test.getUserCategoryProjects(1, 1));
//    	System.out.println(test.createProject("Projekat 5", "opis", 2));
//    	System.out.println(test.requestUsersCellList(3, 0, 10));
//    	System.out.println(test.getProjectById(1));
    	ArrayList<Integer> uids = new ArrayList<Integer>();
    	uids.add(1);
    	uids.add(2);
//    	test.addNewTask(1, "Test task", "simple description", new Date(), new Date());
//    	test.deleteTask(2);
//    	test.removeAssignedTasks(uids, 18);
    	test.changeStatus(1, TaskStatus.FINISHED);
//    	test.removeActivity(17, uids);
//    	test.addActivity(16, uids);
//    	System.out.println(test.requestTasksTable(0, 3, new ColumnSort(ColumnSort.TITLE, true), 1));
    }
    
}
