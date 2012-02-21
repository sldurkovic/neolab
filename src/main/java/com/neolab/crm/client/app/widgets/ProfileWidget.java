package com.neolab.crm.client.app.widgets;

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.SingleUploader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.neolab.crm.client.app.events.ReloadActiveUserEvent;
import com.neolab.crm.client.app.events.SwitchTabEvent;
import com.neolab.crm.client.app.events.UserDeliveryEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.NeoForm;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.client.utils.Util;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.CfgConstants;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.rpc.Response;
import com.neolab.crm.shared.resources.rpc.UploadType;

public class ProfileWidget extends VerticalContainer implements HasAsyncInformation, Requires<User>{
	
	public interface UserProvider{
		void updateUser(User user);
		void requestUser(Requires<User> object);
	}
	
	private static String HOST_EXAMPLE = "example: smtp.gmail.com";
	private static String PROTOCOL_EXAMPLE = "example: smtps";
	
	private User user;
	private UserProvider provider;
	private NeoForm form;
	private TextBox firstName;
	private TextBox lastName;
	private TextBox email;
	private TextBox site;
	private TextBox phone;
	private TextBox password;
	private SingleUploader uploader;
	private String uploaderServletPath;
	
	
	
	public ProfileWidget(User user, UserProvider provider) {
		super(false);
	    uploader = new SingleUploader();
	    uploaderServletPath = uploader.getServletPath();
		this.user = user;
		this.provider = provider;
		render();
		Injector.INSTANCE.getEventBus().addHandler(UserDeliveryEvent.TYPE, new SwitchTabEvent.Handler<UserDeliveryEvent>(){
			@Override
			public void on(UserDeliveryEvent e) {
				delivery(e.getUser());
			    uploader.setServletPath(uploaderServletPath + "?type="+UploadType.PROFILE_IMAGE.toString()+"&uid="+e.getUser().getUid());
			}
		});
		setData();
	}

	@Override
	protected void render() {
		//init
		firstName = new TextBox();
		firstName.addStyleName("neo-TextBox");
		lastName = new TextBox();
		lastName.addStyleName("neo-TextBox");
		email = new TextBox();
		email.addStyleName("neo-TextBox");
		password = new PasswordTextBox();
		password.addStyleName("neo-TextBox");
		site = new TextBox();
		site.addStyleName("neo-TextBox");
		phone = new TextBox();
		phone.addStyleName("neo-TextBox");

		//general info
		DisclosurePanel generalInfo = new DisclosurePanel("General info");

		PushButton saveChanges = new PushButton(Injector.INSTANCE.getConstants().saveChanges());
		saveChanges.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(password.getText().isEmpty()){
					WidgetFactory.error("Password box cannot be empty");
					return;
				}
				user.setFirstName(firstName.getText());
				user.setLastName(lastName.getText());
				user.setEmail(email.getText());
				user.setPassword(password.getText());
				user.setSite(site.getText());
				user.setPhone(phone.getText());
				provider.updateUser(user);
			}
		});
		
		form = new NeoForm();
		form.addField("First name: ", firstName);
		form.addField("Last name: ", lastName);
		form.addField("Email: ", email);
		form.addField("Password: ", password);
		form.addField("Site: ", site);
		form.addField("Phone: ", phone);
		
		 uploader.setServletPath(uploaderServletPath + "?type="+UploadType.PROFILE_IMAGE.toString()+"&uid="+user.getUid());
	    
	  // Load the image in the document and in the case of success attach it to the viewer
	  IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	          Injector.INSTANCE.getEventBus().fireEvent(new ReloadActiveUserEvent());

	        UploadedInfo info = uploader.getServerInfo();
	        GWT.log("File name " + info.name);
	        GWT.log("File content-type " + info.ctype);
	        GWT.log("File size " + info.size);

	        // You can send any customized message and parse it 
	        GWT.log("Server message " + info.message);
	      }
	    }
	  };

	    uploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		form.addField("Profile picture: ", uploader);
		
		form.addSubmitButton(saveChanges);
		addWidget(form);
		generalInfo.add(form);
		generalInfo.addStyleName("margin-Left-30");
		addWidget(generalInfo);
		generalInfo.setOpen(true);
		
		//mail 
		DisclosurePanel mailSettings = new DisclosurePanel("Mail settings");
		mailSettings.addStyleName("margin-Left-30");
		PropertyTable email = new PropertyTable();
		email.setWidth("400px");
		
		final TextBox host = email.insertTextWidget("Host", "200px");

		host.setValue((user.getEmailHost().isEmpty()) ? HOST_EXAMPLE:user.getEmailHost());
		
		final TextBox protocol = email.insertTextWidget("Protocol", null);
		protocol.setText((user.getEmailProtocol().isEmpty()) ? PROTOCOL_EXAMPLE:user.getEmailProtocol());
		
		
		final TextBox port = email.insertTextWidget("Port", null);
		port.setText(String.valueOf(user.getEmailPort()));
		
		final PasswordTextBox epass = email.insertPasswordWidget("Password");
		epass.setText(user.getEmailPassword());
		
		
		
		PushButton save = new PushButton("Save changes");
		save.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				try{
					user.setEmailHost(host.getText());
					user.setEmailProtocol(protocol.getText());
					int p = Integer.parseInt(port.getText());
					user.setEmailPort(p);
					user.setEmailPassword(epass.getText());
					provider.updateUser(user);
				}catch (Exception e) {
					Window.alert("Port must be a number");
				}
			}
		});
		email.insertButton(save);
		mailSettings.add(email);
		addWidget(mailSettings);
		
	}
	
	private void setData(){
		if(user != null){
			firstName.setText(user.getFirstName());
			lastName.setText(user.getLastName());
			email.setText(user.getEmail());
			site.setText(user.getSite());
			phone.setText(user.getPhone());
		}else{
			provider.requestUser(this);
		}
	}

	@Override
	public void showInfo(Response response) {
		form.showInfo(response);
	}

	@Override
	public void delivery(User object) {
		this.user = object;
		setData();
	}
	
	
	
	
}
