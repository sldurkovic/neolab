package com.neolab.crm.server.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.neolab.crm.server.persistance.dao.UsersDAO;
import com.neolab.crm.server.reports.Report;
import com.neolab.crm.shared.resources.Configuration;

public class NeoDownloadServlet extends HttpServlet{
	
	private static Log log = LogFactory.getLog(NeoDownloadServlet.class);

	@Autowired
	private UsersDAO usersDao;
	
	@Autowired
	private Configuration config;
	
	@Autowired
	private NeoServiceImpl service;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
			      config.getServletContext());
	}
	
	 static final int BUFFER_SIZE = 16384;

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        File file = new File(request.getParameter("path"));
	        prepareResponseFor(response, file);
	        streamFileTo(response, file);
	    }

	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	
	    	String pdf = request.getParameter("type");
	    	String date = request.getParameter("date");
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	    	if(pdf != null){
	    		String s = request.getParameter("pid");
	    		String u = request.getParameter("uid");
	    		if(s != null){
	    			int p = Integer.parseInt(s);
			    	Date d;
					try {
						d = sdf.parse(date);		    	
						File temp = Report.parseTasks(service.requestTasksTableByDate(0, Integer.MAX_VALUE, null, 0, p, d).getList(), d);
				        prepareResponseFor(response, temp);
				        streamFileTo(response, temp);
					} catch (ParseException e) {
						log.error(e.getMessage());
					}
	    		}else{
	    			int uid = Integer.parseInt(u);
			    	Date d;
					try {
						d = sdf.parse(date);		    	
						File temp = Report.parseTasks(service.getUserTasksTable(0, Integer.MAX_VALUE, null, -1, uid, false).getList(), d);
				        prepareResponseFor(response, temp);
				        streamFileTo(response, temp);
					} catch (ParseException e) {
						log.error(e.getMessage());
					}
	    		}
	    			

	    		return;
	    	}
	    	String p = request.getParameter("pid");
	    	String document = request.getParameter("document");
	    	
	    	int pid = Integer.parseInt(p);
	    	document = URLDecoder.decode(document,"UTF-8");
	    	log.debug("PID: "+pid);
	    	log.debug("Document: "+document);
	    	File projects = new File(config.getProjectsPath());
	    	log.debug("Path: "+projects.getAbsolutePath());
	    	File[] files = projects.listFiles();
	    	File temp = null;
	    	for(File file : files){
	    		if(file.getName().startsWith(pid+"-")){
	    	    	File[] docs = file.listFiles();
	    	    	for (File file2 : docs) {
						if(file2.getName().equals(document)){
			    			temp = file2;
			    			break;
						}
					}
	    		}
	    			
	    	}
	    	if(temp != null){
		    	log.debug("Temp: "+temp.getAbsolutePath());
	    		
	        prepareResponseFor(response, temp);
	        streamFileTo(response, temp);
	    	}else{
	    		log.debug("Invalid file path");
	    	}
	    }


	    private void streamFileTo(HttpServletResponse response, File file)  throws IOException {
	        OutputStream os = response.getOutputStream();
	        FileInputStream fis = new FileInputStream(file);
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int bytesRead = 0;
	        while ((bytesRead = fis.read(buffer)) > 0) {
	            os.write(buffer, 0, bytesRead);
	        }
	        os.flush();
	        fis.close();
	    }

	    private void prepareResponseFor(HttpServletResponse response, File file) {
	        StringBuilder type = new StringBuilder("attachment; filename=\"");
	        type.append(file.getName());
	        type.append("\"");
	        response.setContentLength((int) file.length());
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", type.toString());
	    }

	
}
