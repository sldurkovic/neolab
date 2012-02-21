package com.neolab.crm.client.app.widgets.projects;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.events.ProjectActivityEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.client.mvp.activities.ProjectsActivity;
import com.neolab.crm.shared.domain.Project;

public class CategoryExplorer extends VerticalContainer {

	interface SelectedProjectHandler {
		void onSelect(Project project);
	}

	private int cid;
	private DeckPanel deck;
	private FlowPanel clouds;
	private ArrayList<Cloud> cloudList;
	private ProjectsActivity presenter;
	private HashMap<Integer, ProjectHolder> map = new HashMap<Integer, ProjectHolder>();

	private Label projectList;
	public CategoryExplorer(int cid, ProjectsActivity presenter) {
		super(false);
		this.presenter = presenter;
		this.cid = cid;
		deck = new DeckPanel();
		clouds = new FlowPanel();
		clouds.addStyleName("projects-Clouds");
		cloudList = new ArrayList<CategoryExplorer.Cloud>();
		projectList = new Label("Project list");
		projectList.setStyleName("big-Title");
		render();
		
		Injector.INSTANCE.getEventBus().addHandler(ProjectActivityEvent.TYPE, new ProjectActivityEvent.Handler<ProjectActivityEvent>() {
			@Override
			public void on(ProjectActivityEvent e) {
				if(!e.isJoin()){
					for(Cloud c : cloudList){
						if(c instanceof ProjectCloud){
							ProjectCloud pc = (ProjectCloud) c;
							if(pc.getProject().getPid() == e.getPid())
								removeCloud(pc);
						}
					}
				}
		}});
		
	}

	@Override
	protected void render() {
		addWidget(clouds);
		clouds.insert(
			projectList,0);
		getTopContainer().setCellHeight(clouds, "10px");
		deck.add(new ProjectListHolder(cid, new SelectedProjectHandler() {
			@Override
			public void onSelect(final Project project) {
				openCloud(project);
			}
		}));
		ProjectListCloud plc = new ProjectListCloud() {
			@Override
			public void onCloudClick() {
				removeStyleName("project-CloudItem-Selected");
				addStyleName("project-CloudItem-Selected");
				deck.showWidget(0);
				for (int i = 1; i < cloudList.size(); i++) {
					ProjectCloud c = (ProjectCloud) cloudList.get(i);
					c.removeStyleName("project-CloudItem-Selected");
					c.hideClose();
				}
			}
		};
		addCloud(plc);
		deck.showWidget(0);
		addWidget(deck);
	}

	private void openProject(Project project) {
		if(map.get(project.getPid()) == null){
			ProjectHolder holder = new ProjectHolder(project, presenter);
			map.put(project.getPid(), holder);
			deck.add(holder);
			deck.showWidget(deck.getWidgetCount() - 1);
		}else{
			deck.showWidget(deck.getWidgetIndex(map.get(project.getPid())));
		}
	}

	public void openCloud(Project project) {
		ProjectListCloud plc = (ProjectListCloud)cloudList.get(0);
		plc.removeStyleName("project-CloudItem-Selected");
		openProject(project);
		addCloud(new ProjectCloud(project) {
			@Override
			public void onCloudClick() {
				deck.showWidget(cloudList.indexOf(this));
				removeStyleName("project-CloudItem-Selected");
				addStyleName("project-CloudItem-Selected");
				ProjectListCloud plc = (ProjectListCloud)cloudList.get(0);
				plc.removeStyleName("project-CloudItem-Selected");
				for (int i = 1; i < cloudList.size(); i++) {
					ProjectCloud c = (ProjectCloud) cloudList.get(i);
					if(c != this){
					c.removeStyleName("project-CloudItem-Selected");
					c.hideClose();
					}else{
						c.showClose();
					}
				}
				show();
			}

			@Override
			public void onCloudClose() {
				removeCloud(this);
			}
		});
	}

	private void addCloud(Cloud cloud) {
		GWT.log("Adding cloud: "+cloud);
		if(cloud instanceof ProjectCloud){
			ProjectCloud pcloud = (ProjectCloud) cloud;
			pcloud.addStyleName("project-CloudItem-Selected");
			for (int i = 1; i < cloudList.size(); i++) {
				ProjectCloud c = (ProjectCloud) cloudList.get(i);
				if(c != pcloud){
					c.removeStyleName("project-CloudItem-Selected");
					c.hideClose();
				}else{
					c.showClose();
				}
			}
			for (Cloud c : cloudList) {
				if(cloudList.indexOf(c) != 0){
					ProjectCloud pc = (ProjectCloud) c;
					if(pc.getProject().getPid() == pcloud.getProject().getPid()){
						c.onCloudClick();
						return;
					}
				}
			}
		}
		cloudList.add(cloud);
		if(cloudList.size() == 1){
			cloud.hide();
			projectList.setVisible(true);
		}
		if(cloudList.size() == 2){
			projectList.setVisible(false);
			cloudList.get(0).show();
		}
			
		int index = 0;
		if (clouds.getWidgetCount() > 0)
			index = clouds.getWidgetCount();
		clouds.insert((Widget) cloud, index);
	}

	private void removeCloud(ProjectCloud cloud) {
		map.remove(cloud.getProject().getPid());
		GWT.log("Destroying cloud: "+cloud);
		deck.remove(cloudList.indexOf(cloud));
		cloudList.remove(cloud);
		if(cloudList.size() == 1){
			cloudList.get(0).hide();
			projectList.setVisible(true);
		}else{
			ProjectCloud pc = (ProjectCloud)cloudList.get(cloudList.size()-1);
			pc.addStyleName("project-CloudItem-Selected");
//			pc.showClose();
		}
		cloud.destroy();
		deck.showWidget(deck.getWidgetCount()-1);
	}

	private abstract class ProjectCloud extends Composite implements Cloud {

		private HorizontalPanel container;
		private InlineLabel label;
		private Image closeButton;
		private Project project;

		public ProjectCloud(final Project project) {
			container = new HorizontalPanel ();
			initWidget(container);
			container.sinkEvents(Event.ONCLICK);
			container.sinkEvents(Event.MOUSEEVENTS);
			container.setTitle(project.getTitle());
			this.project = project;
			container.addHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					onCloudClick();
				}
			}, ClickEvent.getType());
			container.addHandler(new MouseOverHandler() {
				
				@Override
				public void onMouseOver(MouseOverEvent event) {
					showClose();
				}
			}, MouseOverEvent.getType());
			container.addHandler(new MouseOutHandler() {
				
				@Override
				public void onMouseOut(MouseOutEvent event) {
//					if (!container.getStyleName().equals("project-CloudItem inline-block hand project-CloudItem-Selected")) {
						hideClose();
//					}
				}
			}, MouseOutEvent.getType());
			container.setStyleName("project-CloudItem");
			container.addStyleName("inline-block");
			label = new InlineLabel(project.getTitle());
			container.add(label);
			// container.setCellVerticalAlignment(label,
			// HasVerticalAlignment.ALIGN_MIDDLE);
			closeButton = new Image("images/project_suppress.png");
			closeButton.setStyleName("project-CloudItem-Close");
			addStyleName("hand");
			container.add(closeButton);
			container.setCellWidth(closeButton, "20px");
			container.setCellVerticalAlignment(closeButton, HasVerticalAlignment.ALIGN_MIDDLE);
			// container.setCellVerticalAlignment(closeButton,
			// HasVerticalAlignment.ALIGN_MIDDLE);
			closeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					onCloudClose();
				}
			});
			hideClose();

		}
		
		public void hideClose() {
			closeButton.setVisible(false);
		}
		
		public void showClose() {
			closeButton.setVisible(true);
		}

		public Project getProject(){
			return project;
		}

		@Override
		public void hide() {
			setVisible(false);
		}

		@Override
		public void show() {
			setVisible(true);			
		}

		@Override
		public void destroy() {
			removeFromParent();
		}

		@Override
		public String toString() {
			return "ProjectCloud [label=" + label + ", project=" + project
					+ "]";
		}

		public abstract void onCloudClose();
		
	}

	private abstract class ProjectListCloud extends Composite implements Cloud {

		private FlowPanel container;
		private InlineLabel label;
		private Image listInstances;

		public ProjectListCloud() {
			container = new FlowPanel();
			container.sinkEvents(Event.ONCLICK);
			container.addHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					onCloudClick();
				}
			}, ClickEvent.getType());
			container.setTitle("Project list");

			initWidget(container);
			container.setStyleName("project-CloudItem");
			container.addStyleName("inline-block");
			setHeight("26px");
			addStyleName("hand");
			label = new InlineLabel("");
			listInstances = new Image("images/list_instances.png");
			container.add(listInstances);
			container.add(label);
			// container.setCellVerticalAlignment(label,
			// HasVerticalAlignment.ALIGN_MIDDLE);
		}

		@Override
		public void hide() {
			setVisible(false);
		}

		@Override
		public void show() {
			setVisible(true);
		}

		@Override
		public void destroy() {
			removeFromParent();
		}

		@Override
		public String toString() {
			return "ProjectListCloud [label=" + label + "]";
		}
	}

	private interface Cloud {
		void hide();

		void show();

		void destroy();

		void onCloudClick();
	}


}
