package com.neolab.crm.client.fwk;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

public class ConfirmDialog extends FormDialog{
	
	public interface RequiresUserResponse{
		void onResponse(boolean response);
	}

	public ConfirmDialog(final RequiresUserResponse caller, String question, String yes, String no) {
		Label label = new Label(question);
		label.addStyleName("formDialog-Light-Title");
		addWidget(label);
		addStyleName("z-Index-10");
		setGlassStyleName("glass-Second-Level");
		addButton(yes, new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
				caller.onResponse(true);
			}
		});
		addButton(no, new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
				caller.onResponse(false);
			}
		});
	}
	
}
