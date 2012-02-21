package com.neolab.crm.server.services;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.neolab.crm.server.persistance.dao.ProjectsDAO;
import com.neolab.crm.server.persistance.dao.UsersDAO;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.Configuration;
import com.neolab.crm.shared.resources.rpc.UploadType;

@SuppressWarnings("serial")
public class NeoUploadServlet extends UploadAction {
	
	@Autowired
	private UsersDAO usersDao;
	
	@Autowired 
	private ProjectsDAO projectsDao;
	
	@Autowired
	private Configuration config;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
			      config.getServletContext());

	}

	private static Log log = LogFactory.getLog(NeoUploadServlet.class);

	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	/**
	 * Maintain a list with received files and their content types.
	 */
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		
		
//		String projectsPath =  getServletContext().getRealPath("projects/");
		String projectsPath = config.getProjectsPath();
		User user = usersDao.getUser(Integer.parseInt(request.getParameter("uid")));

		
		String response = "";
		UploadType type = UploadType.valueOf(request.getParameter("type").toUpperCase());
		log.debug(request.getParameter("uid"));
		int cont = 0;
		log.debug("Upload servlet recieved request");

		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				cont++;
				try {

					// / Create a temporary file placed in the default system
					// temp folder
					switch (type) {
					case PROFILE_IMAGE:
						log.debug("Processing user image change");
						String contextPath =  getServletContext().getRealPath("images/users/");
						File temp = new File(contextPath+user.getImage());
						if(!user.getImage().equals("profile_default.png"))
							temp.delete();
						log.debug("path="+contextPath+item.getName());
						File file= new File(contextPath+user.getUid()+"-"+item.getName());
						file.createNewFile();
						item.write(file);
						user.setImage(user.getUid()+"-"+item.getName());
						usersDao.update(user);

//						File file = File.createTempFile("upload-", ".bin");
//						item.write(file);
						// / Save a list with the received files
						receivedFiles.put(item.getFieldName(), temp);
						receivedContentTypes.put(item.getFieldName(),
								item.getContentType());

						// / Send a customized message to the client.
						response += "File saved as " + temp.getAbsolutePath();
						break;
					case PROJECT_DOCUMENTS:
						log.debug("Processing project document");
						int pid = Integer.parseInt(request.getParameter("pid"));
						Project p = projectsDao.getProject(pid);
						File projectTempFolder = new File(projectsPath+p.getPid()+"-"+p.getTitle());
						log.debug("[creating dir] "+projectTempFolder.getAbsolutePath());
						projectTempFolder.mkdir();
						
						log.debug("path=");
						File projectFile = new File(projectTempFolder+"\\"+item.getName());
						projectFile.createNewFile();
						item.write(projectFile);

						// / Save a list with the received files
						receivedFiles.put(item.getFieldName(), projectFile);
						receivedContentTypes.put(item.getFieldName(),
								item.getContentType());

						// / Send a customized message to the client.
						response += "File saved as " + projectFile.getAbsolutePath();
						break;
					default:
						break;
					}


				} catch (Exception e) {
					log.debug(e);
					e.printStackTrace();
					throw new UploadActionException(e);
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send your customized message to the client.
		return response;
	}

	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String fieldName = request.getParameter(PARAM_SHOW);
		File f = receivedFiles.get(fieldName);
		if (f != null) {
			response.setContentType(receivedContentTypes.get(fieldName));
			FileInputStream is = new FileInputStream(f);
			copyFromInputStreamToOutputStream(is, response.getOutputStream());
		} else {
			renderXmlResponse(request, response, ERROR_ITEM_NOT_FOUND);
		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null) {
			file.delete();
		}
	}

}
