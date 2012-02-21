package com.neolab.crm.client.fwk;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neolab.crm.client.fwk.containers.FlowContainer;

public class Upload extends FlowContainer {

	public Upload() {
		super(true);
	}

	protected void render() {
		final FormPanel form = new FormPanel();
		form.setAction("/fileupload");

		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

//		// Create a TextBox, giving it a name so that it will be submitted.
//		final TextBox tb = new TextBox();
//		tb.setName("textBoxFormElement");
//		panel.add(tb);


		// Create a FileUpload widget.
		FileUpload upload = new FileUpload();
		upload.setName("file");
		panel.add(upload);

		// Add a 'submit' button.
		Button submit = new Button("Submit");
		submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				form.submit();
			}
		});
		panel.add(submit);
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Window.alert(event.getResults());
			}
		});
		
//		// Add an event handler to the form.
//		form.addFormHandler(new FormHandler() {
//			public void onSubmit(FormSubmitEvent event) {
//				// This event is fired just before the form is submitted. We can
//				// take
//				// this opportunity to perform validation.
//				if (tb.getText().length() == 0) {
//					Window.alert("The text box must not be empty");
//					event.setCancelled(true);
//				}
//			}
		addWidget(form);
	}

}
