package com.neolab.crm.client.app.widgets;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.HideDialogsEvent;
import com.neolab.crm.client.app.events.ProjectUserChangeEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.dialogs.AssignTaskDialog;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.mvp.places.ProjectsPlace;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.Option;
import com.neolab.crm.shared.resources.rpc.ObjectResponse;
import com.neolab.crm.shared.resources.rpc.Response;

public class UserPreviewWidget extends FormDialog implements
		HasAsyncInformation {

	private User user;
	private DisclosurePanel emailPanel;
	private FlexTable email = new FlexTable();
	private Project project;
	private int level;

	public UserPreviewWidget(User user) {
		this(null, user, 1);
	}

	public UserPreviewWidget(Project project, User user, int level) {
		this.user = user;
		this.project = project;
		this.level = level;
		if(level == 2){
			setGlassStyleName("glass-Second-Level");
			addStyleName("z-Index-10");
		}
		render();
		setPopupPosition(Window.getClientWidth() / 2 - 300, 70);
		show();
	}

	protected void render() {

		// profile preview
		HorizontalPanel preview = new HorizontalPanel();
		Image image = ImageFactory.getMediumUserImage(user.getImage());
		preview.add(image);
		// preview.setCellHorizontalAlignment(image,
		// HasHorizontalAlignment.ALIGN_LEFT);

		FlexTable table = new FlexTable();
		table.getFlexCellFormatter().setRowSpan(0, 0, 3);
		table.setWidget(0, 0, image);

		// glue
		table.getFlexCellFormatter().setRowSpan(0, 1, 3);
		HTML glue = new HTML("&nbsp;");
		table.setWidget(0, 1, glue);
		table.getFlexCellFormatter().setWidth(0, 1, "30px");

		Label fln = new Label(user.getFirstName() + " " + user.getLastName());
		fln.addStyleName("user-Big-Title");
		//
		// String title = "Admin";
		// if(user.getLevel() == 2){
		// title = "Moderator";
		// }
		Label role = new Label(Injector.INSTANCE.getApplication().getLevelName(
				user.getLevel()));
		role.setStyleName("user-Sub-Title");
		table.setWidget(0, 2, fln);
		table.getFlexCellFormatter().setHeight(0, 2, "10px");
		table.setWidget(1, 0, role);
		table.getFlexCellFormatter().setVerticalAlignment(1, 0,
				HasVerticalAlignment.ALIGN_TOP);
		// table.getFlexCellFormatter().setStyleName(1, 0,
		// "user-Preview-Small-Text");

		PushButton sendMail = new PushButton("Send mail", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				email.setVisible(!email.isVisible());
			}
		});

		PushButton remove = new PushButton("Remove from project",
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						ArrayList<Integer> uids = new ArrayList<Integer>();
						uids.add(user.getUid());
						Injector.INSTANCE.getNeoService().removeActivity(
								project.getPid(), uids,
								new AbstractAsyncCallback<Response>() {
									@Override
									public void success(Response result) {
										WidgetFactory.alert("User removed from project");
										Injector.INSTANCE
												.getEventBus()
												.fireEvent(
														new ProjectUserChangeEvent(
																project.getPid()));
										hide();
										if(level == 2)
											UserPreviewWidget.this.removeStyleName("z-Index-10");

									}
								});
					}
				});
		PushButton assignTask = new PushButton("Assign task",
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						new AssignTaskDialog(project, user.getUid());
					}
				});

		HorizontalPanel options = new HorizontalPanel();
		options.add(sendMail);
		if(project != null){
			if(Injector.INSTANCE.getApplication().hasPrivilege(Option.REMOVE_FROM_PROJECT)){
				WidgetFactory.glue(options);
				options.add(remove);
			}
			if(Injector.INSTANCE.getApplication().hasPrivilege(Option.ASSIGN_TASK)){
				WidgetFactory.glue(options);
				options.add(assignTask);
			}
		}

		table.setWidget(2, 0, options);

		HTML glue1 = new HTML("&nbsp;");
		table.setWidget(3, 0, glue1);
		table.getFlexCellFormatter().setHeight(3, 0, "5px");
		preview.add(table);
		// addWidget(table);
		preview.setCellHorizontalAlignment(table,
				HasHorizontalAlignment.ALIGN_LEFT);

		PropertyTable details = new PropertyTable();
		details.setWidth("500px");
		details.insertWidget("Email", user.getEmail());
		if (!user.getPhone().isEmpty())
			details.insertWidget("Phone number", user.getPhone());

		if (!user.getSite().isEmpty())
			details.insertWidget("Web site", WidgetFactory.getHyperlink(
					user.getSite(), user.getSite(), true));

		final SimplePanel ps = new SimplePanel();
		ps.setWidget(ImageFactory.smallLoading());
		details.insertWidget("Projects", ps);
		Injector.INSTANCE
				.getNeoService()
				.getUserProjects(
						user.getUid(),
						new AbstractAsyncCallback<ObjectResponse<ArrayList<Project>>>() {

							@Override
							public void success(
									ObjectResponse<ArrayList<Project>> result) {
								if (result != null) {
									FlowPanel panel = new FlowPanel();
									ArrayList<Project> res = result.getObject();
									if(res.size() == 0)
										panel.add(new Label("none"));
									else
									for (Project p : res) {
										Label link = WidgetFactory
												.getInternalHyperlink(
														p.getTitle(),
														new ProjectsPlace(p
																.getPid()));
										link.addClickHandler(new ClickHandler() {

											@Override
											public void onClick(ClickEvent event) {
//												hide();
												Injector.INSTANCE.getEventBus().fireEvent(new HideDialogsEvent());
												if(level == 2)
												UserPreviewWidget.this.removeStyleName("z-Index-10");
											}
										});
										panel.add(link);
										HTML sep = new HTML("&nbsp;-&nbsp;");
										sep.addStyleName("display-InlineBlock");
										if (res.indexOf(p) != res.size() - 1)
											panel.add(sep);
									}
									ps.clear();
									ps.setWidget(panel);
								}
							}
						});

		// VerticalPanel vp = new VerticalPanel();

		// email = new FlexTable();

		email.addStyleName("margin-Bottom-20");
		email.setVisible(false);
		// email.setWidth("350px");
		final TextBox subject = new TextBox();
		subject.addStyleName("neo-TextBox");
		subject.setWidth("275px");
		final TextArea body = new TextArea();
		body.addStyleName("neo-TextBox");
		body.setWidth("400px");
		body.setHeight("100px");
		Label l = new Label("Subject: ");
		l.setWidth("70px");
		email.setWidget(0, 0, l);
		email.setWidget(0, 1, subject);
		email.getFlexCellFormatter().setColSpan(1, 0, 2);
		email.setWidget(1, 0, body);

		// submit
		email.getFlexCellFormatter().setColSpan(2, 0, 2);
		Panel p = WidgetFactory.getBigButton("Send", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Injector.INSTANCE.getNeoService().sendMail(user.getEmail(), -1,
						subject.getText(), body.getText(),
						new AbstractAsyncCallback<Response>() {
							public void success(Response result) {
								showInfo(result);
							};
						});
			}
		});
		email.setWidget(2, 0, p);
		email.getFlexCellFormatter().setAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		// emailPanel.add(email);

		// addWidget(sendMail);
		addWidget(preview);
		getContent().setCellHorizontalAlignment(preview,
				HasHorizontalAlignment.ALIGN_LEFT);

		// addWidget(sendMail);
		addWidget(email);
		getContent().setCellHorizontalAlignment(email,
				HasHorizontalAlignment.ALIGN_CENTER);

		addWidget(details);
		getContent().setCellHorizontalAlignment(details,
				HasHorizontalAlignment.ALIGN_LEFT);

		// projects
		// DisclosurePanel projects = new DisclosurePanel("Projects");
		// projects.add(new Label("Projects on which user is working on"));
		// addWidget(projects);
		// getContent().setCellHorizontalAlignment(projects,
		// HasHorizontalAlignment.ALIGN_LEFT);
		// projects.setWidth("400px");

		// email
		emailPanel = new DisclosurePanel("Email");
		emailPanel.setWidth("400px");

		emailPanel.setOpen(true);
		emailPanel.setVisible(false);
		addWidget(emailPanel);
		getContent().setCellHorizontalAlignment(emailPanel,
				HasHorizontalAlignment.ALIGN_LEFT);
		center();

	}

	public void center() {

		int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		int top = (Window.getClientHeight() - getOffsetHeight()) >> 1;
		setPopupPosition(Math.max(Window.getScrollLeft() + left - 200, 0),
				Math.max(Window.getScrollTop() + top - 200, 0));
	}

	@Override
	public void showInfo(Response response) {
		if (response.getStatus()) {
			Window.alert(response.getMsg());
		} else {
			Window.alert(response.getMsg());
		}
	}
}
