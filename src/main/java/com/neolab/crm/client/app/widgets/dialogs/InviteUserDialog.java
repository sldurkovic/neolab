package com.neolab.crm.client.app.widgets.dialogs;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.neolab.crm.client.app.events.HideDialogsEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.shared.resources.rpc.Response;

public class InviteUserDialog extends FormDialog implements HasAsyncInformation {

	public interface InviteHandler {
		void invite(String name, String email, int level,
				HasAsyncInformation caller);
	}

	private InviteHandler handler;
	private TextBox name;
	private TextBox email;
	private ListBox list;

	public InviteUserDialog(InviteHandler handler) {
		this.handler = handler;
		renderPanel();
		int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}

	private void renderPanel() {
		Label head = new Label("Invite user");
		head.addStyleName("formDialog-Title");
		addWidget(head);

		email = new TextBox();
		email.addStyleName("neo-TextBox");
		name = new TextBox();
		name.addStyleName("neo-TextBox");
		list = new ListBox();
		list.addStyleName("neo-ListBox");
		ArrayList<String> levels = Injector.INSTANCE.getApplication()
				.getLevels();
		
		for (String string : levels) {
			list.addItem(string);
		}
		

		FlexTable inviteBox = new FlexTable();
		inviteBox.setWidth("300px");
		inviteBox.setWidget(0, 0, new Label("Name: "));
		inviteBox.setWidget(0, 1, name);
		inviteBox.setWidget(1, 0, new Label("Email: "));
		inviteBox.setWidget(1, 1, email);
		inviteBox.setWidget(2, 0, new Label("Level: "));
		inviteBox.setWidget(2, 1, list);
		inviteBox.setWidget(4, 0, WidgetFactory.getBigButton("Send", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (email.getText().isEmpty())
					WidgetFactory.alert("Please enter email");
				else
					handler.invite(name.getText(), email.getText(), 0,
							InviteUserDialog.this);
			}
		}));
		inviteBox.setWidget(3, 0, new HTML("&nbsp;"));
		inviteBox.getFlexCellFormatter().setColSpan(3, 0, 2);
		inviteBox.getCellFormatter().setAlignment(4, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		inviteBox.getFlexCellFormatter().setColSpan(4, 0, 2);
		addWidget(inviteBox);
	}

	@Override
	public void showInfo(Response response) {
		WidgetFactory.alert("all ok");
		Injector.INSTANCE.getEventBus().fireEvent(new HideDialogsEvent());
	}

}
