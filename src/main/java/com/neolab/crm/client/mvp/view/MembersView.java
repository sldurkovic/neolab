package com.neolab.crm.client.mvp.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.dialogs.InviteUserDialog;
import com.neolab.crm.client.app.widgets.tables.EditUsersTable;
import com.neolab.crm.client.app.widgets.tables.UsersTable;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.mvp.BaseView;
import com.neolab.crm.client.mvp.activities.MembersActivity;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.Option;

public class MembersView extends BaseView<MembersActivity> {

	private EditUsersTable table;
	private UsersTable basicTable;
	private DeckPanel tables;

	public MembersView(MembersActivity p) {
		super(p);
		render();
	}

	@Override
	public void render() {
		// container = new HorizontalPanel();
		// getTopContainer().add(container);
		//
		// FlexTable sidebar = new FlexTable();
		// sidebar.setStyleName("user-Sidebar");
		// for (int i = 0; i < 4; i++) {
		// sidebar.setWidget(i, 0, new Label("category"+i));
		// }
		// addWidget(sidebar);
		tables = new DeckPanel();

		// final DisclosurePanel members = new DisclosurePanel("Members");
		// members.setOpen(true);
		table = new EditUsersTable(10, false, true, adminBar(), new EditUsersTable.UpdateUserProvider() {
			
			@Override
			public void update(ArrayList<User> list, HasAsyncInformation caller) {
				presenter.updateUsers(list, caller);
			}
		});
		// addWidget(table);
		basicTable = new UsersTable(10, false, true, tableBar());
		tables.add(basicTable);
		tables.add(table);
		tables.showWidget(0);

		adminBar();
		addWidget(tables);
		tables.addStyleName("members-Tables");
		setWidth("100%");
		getTopContainer().setCellHorizontalAlignment(tables,
				HasHorizontalAlignment.ALIGN_CENTER);
	}

	private ArrayList<PushButton> adminBar(){
		ArrayList<PushButton> list = new ArrayList<PushButton>();
		if(activeUser.getLevel() == 1){
			HorizontalPanel admin = new HorizontalPanel();
			final PushButton adminButton = new PushButton("Back to regular table");
			adminButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					tables.showWidget(0);
				}
			});
			list.add(adminButton);
			final PushButton invite = new PushButton("Invite new user", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					new InviteUserDialog(new InviteUserDialog.InviteHandler() {
						
						@Override
						public void invite(String name, String email, int level,
								HasAsyncInformation caller) {
							presenter.registerUser(name, email, level, caller);
						}
					});
				}
			});
			list.add(invite);
		}
		
		return (list.size() >0)? list:null;
	}
	
	private ArrayList<PushButton> tableBar(){
		ArrayList<PushButton> list = new ArrayList<PushButton>();
		if(activeUser.getLevel() == 1){
			HorizontalPanel admin = new HorizontalPanel();
			final PushButton adminButton = new PushButton("Edit table");
			adminButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					tables.showWidget(1);
				}
			});
			if(Injector.INSTANCE.getApplication().hasPrivilege(Option.EDIT_USER)){
				list.add(adminButton);
			}
			final PushButton invite = new PushButton("Invite new user", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					new InviteUserDialog(new InviteUserDialog.InviteHandler() {
						
						@Override
						public void invite(String name, String email, int level,
								HasAsyncInformation caller) {
							presenter.registerUser(name, email, level, caller);
						}
					});
				}
			});
			if(Injector.INSTANCE.getApplication().hasPrivilege(Option.INVITE_USER)){
				list.add(invite);
			}
		}
		
		return (list.size() >0)? list:null;
	}
}
