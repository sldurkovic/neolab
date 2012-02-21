package com.neolab.crm.client.app.base;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;

public class ImageFactory {

	private static final String IMAGES_URL = "images/";
	private static final String USERS_IMAGES_URL = "images/users/";

	public static Image create(String fileName, String width, String height,
			String style) {
		return createImage(toURL(fileName), width, height, style);
	}

	public static String toURL(String imageFile) {
		return IMAGES_URL + imageFile;
	}

	public static Image create(ImageResource resource) {
		return new Image(resource);
	}
	

	private static final String NEOLOGO = "logo.png";
	public static Image neologo() {
		return createImage(IMAGES_URL+NEOLOGO, "264px", "55px", null);
	}
	

	private static final String DOCUMENT = "data_property_small.png";
	public static Image document() {
		return createImage(IMAGES_URL+DOCUMENT, "15px", "4px", null);
	}

	private static final String PROFILE_DEFAULT = "profile_default.png";
	public static Image profileDefault() {
		return createImage(USERS_IMAGES_URL+PROFILE_DEFAULT, "190px", "200px", null);
	}
	
	private static final String SMALL_LOADING = "small_loading.gif";
	public static Image smallLoading() {
		return createImage(IMAGES_URL+SMALL_LOADING, "16px", "16px", null);
	}
	
	private static final String X = "veryred-x.png";
	public static Image x() {
		return create(X, "15px", "15px", null);
	}
	
	private static final String DOWNLOAD = "download.png";
	public static Image download() {
		return create(DOWNLOAD, "10px", "7px", null);
	}
	
	
	public static Image getUserImage(String name){
		return createImage(USERS_IMAGES_URL+name, "190px", "200px", null);
//		return new Image(USERS_IMAGES_URL+name,);
	}
	
	public static Image getMediumUserImage(String image) {
		return createImage(USERS_IMAGES_URL+image, "100px", "100px", null);
	}

	public static Image getSmallUserImage(String image) {
		return createImage(USERS_IMAGES_URL+image, "35px", "35px", null);
	}
	
	private static final String PROJECT_SMALL = "instance_small.gif";
	public static Image projectSmallIco() {
		return create(PROJECT_SMALL, "15px", "16px", null);
	}
	
	private static final String CLASS_SMALL = "class_small.png";
	public static Image classSmall() {
		return create(CLASS_SMALL, "14px", "14px", null);
	}
	
	private static final String GREEN_PLUS = "green_plus.png";
	public static Image greenPlus() {
		return create(GREEN_PLUS, "14px", "14px", null);
	}
	
	private static final String RETRACT = "retract.png";
	public static Image retract() {
		return create(RETRACT, "27px", "17px", null);
	}

	
	private static final String EXPAND = "expand.png";
	public static Image expand() {
		return create(EXPAND, "27px", "17px", null);
	}
	
	
	private static final String TO_DO = "Todo.png";
	public static Image toDo() {
		return create(TO_DO, "40px", "40px", null);
	}
	
	private static final String CALENDAR = "calendar.png";
	public static Image calendar() {
		return create(CALENDAR, "40px", "40px", null);
	}
	
	private static final String DOCUMENTS = "documents.png";
	public static Image documents() {
		return create(DOCUMENTS, "40px", "40px", null);
	}
	
	


	private static Image createImage(String url, String width, String height,
			String... styles) {
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException(
					"URL for image must not be empty");
		}
		Image img = new Image(url);
		if (styles != null) {
			for (String style : styles) {
				if (style != null) {
					img.addStyleName(style);
				}
			}
		}
		if (width != null && !width.isEmpty()) {
			img.setWidth(width);
		}
		if (height != null && !height.isEmpty()) {
			img.setHeight(height);
		}
		return img;
	}


}
