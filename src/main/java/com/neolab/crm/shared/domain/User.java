package com.neolab.crm.shared.domain;

import java.io.Serializable;
import java.util.Set;

import com.google.gwt.view.client.ProvidesKey;
import com.neolab.crm.shared.resources.Mergeable;

@SuppressWarnings("serial")
public class User implements Serializable, Mergeable<User>{
	
	private int uid;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Integer level;
	private String image;
	private String phone;
	private String site;
	private Set<TaskActivity> activity;
	private String emailHost;
	private Integer emailPort;
	private String emailProtocol;
	private String emailPassword;
	
	public User() {
	}
	
	public Set<TaskActivity> getActivity() {
		return activity;
	}

	public void setActivity(Set<TaskActivity> activity) {
		this.activity = activity;
	}

	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}

	public static final ProvidesKey<User> KEY_PROVIDER = new ProvidesKey<User>() {
        public Object getKey(User item) {
          return item == null ? null : item.getUid();
        }
      };

	@Override
	public String toString() {
		return "User [uid=" + uid + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", password=" + password
				+ ", level=" + level + ", image=" + image + ", phone=" + phone
				+ ", site=" + site + "email_password="+emailPassword+"]";
	}
	
	
	
	public String getEmailHost() {
		return emailHost;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}

	public Integer getEmailPort() {
		return emailPort;
	}

	public void setEmailPort(Integer emailPort) {
		this.emailPort = emailPort;
	}

	public String getEmailProtocol() {
		return emailProtocol;
	}

	public void setEmailProtocol(String emailProtocol) {
		this.emailProtocol = emailProtocol;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	@Override
	public void merge(User object) {
		if(object.getFirstName() != null && !object.getFirstName().equals(firstName))
			firstName = object.getFirstName();

		if(object.getLastName() != null && !object.getLastName().equals(lastName))
			lastName = object.getLastName();

		if(object.getPassword()!= null && !object.getPassword().equals(password))
			password = object.getPassword();

		if(object.getEmail() != null && !object.getEmail().equals(email))
			email = object.getEmail();
		
		if(object.getImage() != null && !object.getImage().equals(image))
			image = object.getImage();
		
		if(object.getSite() != null && !object.getSite().equals(site))
			site = object.getSite();
		
		if(object.getPhone() != null && !object.getPhone().equals(phone))
			phone = object.getPhone();
		
		if(object.getLevel() != 0 && object.getLevel() != level)
			level = object.getLevel();
		

		if(object.getEmailHost() != null && !object.getEmailHost().equals(emailHost))
			emailHost = object.getEmailHost();
		

		if(object.getEmailProtocol() != null && !object.getEmailProtocol().equals(emailProtocol))
			emailProtocol = object.getEmailProtocol();
		

		if(object.getEmailPort() != null && !object.getEmailPort().equals(emailPort))
			emailPort = object.getEmailPort();
		

		if(object.getEmailPassword() != null && !object.getEmailPassword().equals(emailPassword))
			emailPassword = object.getEmailPassword();
	}
	
}
