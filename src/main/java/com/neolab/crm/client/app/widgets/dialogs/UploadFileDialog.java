package com.neolab.crm.client.app.widgets.dialogs;

import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.neolab.crm.client.app.events.NewProjectEvent;
import com.neolab.crm.client.app.events.ProjectUpdatedEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.rpc.UploadType;

public class UploadFileDialog extends FormDialog{
	
	private Project project;
	
	public UploadFileDialog(Project project) {
		this.project = project;
		renderPanel();
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}

	private void renderPanel() {
		Label head = new Label("Upload file");
		head.addStyleName("formDialog-Title");
		addWidget(head);
		
		SingleUploader uploader = new SingleUploader();
		String uploaderServletPath = uploader.getServletPath();
		uploader.setServletPath(uploaderServletPath + "?type="
				+ UploadType.PROJECT_DOCUMENTS.toString() + "&uid="
				+ Injector.INSTANCE.getApplication().getActiveUser().getUid());
		uploader.setServletPath(uploader.getServletPath() + "&pid=" + project.getPid());
		addWidget(uploader);
		uploader.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
			@Override
			public void onFinish(IUploader uploader) {
				Injector.INSTANCE.getEventBus()
				.fireEvent(new ProjectUpdatedEvent(project.getPid()));
			}
		});
	}
	
	

}
