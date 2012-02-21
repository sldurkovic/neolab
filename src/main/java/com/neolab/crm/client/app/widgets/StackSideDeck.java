package com.neolab.crm.client.app.widgets;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.NewProjectEvent;
import com.neolab.crm.client.app.events.ProjectActivityEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.dialogs.NewCategoryDialog;
import com.neolab.crm.client.app.widgets.dialogs.NewCategoryDialog.CategoryProvider;
import com.neolab.crm.client.app.widgets.projects.CategoryExplorer;
import com.neolab.crm.client.app.widgets.projects.UserCategoryProjects;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.fwk.containers.HorizontalContainer;
import com.neolab.crm.client.mvp.view.ProjectsView.UserCategoryProjectsProvider;
import com.neolab.crm.shared.domain.Category;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.Option;
import com.neolab.crm.shared.resources.Requires;

public class StackSideDeck extends HorizontalContainer implements Requires<UserCategoryProjects>{

	private DeckPanel deck;
	private DecoratedStackPanel stackPanel;
	private int selected;
	private UserCategoryProjectsProvider handler;
	private HashMap<Integer, SimplePanel> projects;
	private CategoryProvider provider;
	private ArrayList<Category> categories; 
	
	public StackSideDeck(UserCategoryProjectsProvider handler, NewCategoryDialog.CategoryProvider provider) {
		super(false);	
		this.provider = provider;
		this.handler = handler;
		categories = new ArrayList<Category>();
		projects = new HashMap<Integer, SimplePanel>();
		render();
	}

	@Override
	protected void render() {
		deck = new DeckPanel();
		Injector.INSTANCE.getEventBus().addHandler(NewProjectEvent.TYPE, new NewProjectEvent.Handler<NewProjectEvent>() {
			@Override
			public void on(NewProjectEvent e) {
				handler.getUserCategoryProjects(e.getCategory(), StackSideDeck.this);
			}
		});
		Injector.INSTANCE.getEventBus().addHandler(ProjectActivityEvent.TYPE, new ProjectActivityEvent.Handler<ProjectActivityEvent>() {
			@Override
			public void on(ProjectActivityEvent e) {
				handler.getUserCategoryProjects(stackPanel.getSelectedIndex()+1, StackSideDeck.this);
			}
		});
		stackPanel = new DecoratedStackPanel(){
			public void showStack(int index) {
				super.showStack(index);
				selected = index;
				handler.getUserCategoryProjects(selected+1, StackSideDeck.this);
				selectedMenuItemHandler(index);
			}
		};
		stackPanel.setWidth("200px");
		VerticalPanel sidebar = new VerticalPanel();
		sidebar.add(stackPanel);
		WidgetFactory.glue(sidebar, "10px");
		
		if(Injector.INSTANCE.getApplication().hasPrivilege(Option.CREATE_CATEGORY))
		sidebar.add(new PushButton("New category", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new NewCategoryDialog(provider);
			}
		}));
		addWidget(sidebar);
		addWidget(deck);
		deck.addStyleName("projects-Central");
		getTopContainer().setCellWidth(deck, "100%");
	}
	
	public void addView(Category cat, CategoryExplorer content) {
		deck.add(content);
		SimplePanel panel = new SimplePanel();
		panel.setWidget(ImageFactory.smallLoading());
		projects.put(stackPanel.getWidgetCount(), panel);
		categories.add(cat);
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
	      sb.appendHtmlConstant("<table style=\"height: 100%;\">");

	      // Add the contact image.
	      sb.appendHtmlConstant("<tr><td style=\"vertical-align:middle;\">");
	      // Add the contact image.
	      sb.appendHtmlConstant("<img  style=\"vertical-align:middle;\" src=\"images/class_small.png\" alt=\"\" /> &nbsp;");
	      sb.appendHtmlConstant("</td>");

	      // Add the name and address.
	      sb.appendHtmlConstant("<td >");
	      sb.appendEscaped(cat.getTitle());
	      sb.appendHtmlConstant("</td></tr></table>");
	      

		stackPanel.add(panel, sb.toSafeHtml());
		deck.showWidget(0);
	}
	
	public CategoryExplorer getCategoryExplorer(int cid){
		return (CategoryExplorer) deck.getWidget(cid -1);
	}
	

	private void selectedMenuItemHandler(int index) {
		deck.showWidget(index);
	};
	
	public CategoryExplorer getVisibleExplorer(){
		return (CategoryExplorer)deck.getWidget(deck.getVisibleWidget());
	}
	
	public int getSelectedIndex(){
		return selected;
	}

	@Override
	public void delivery(UserCategoryProjects object) {
		GWT.log("delivering="+object);
		projects.get(object.getCat()-1).clear();
		projects.get(object.getCat()-1).setWidget(parseProjects(object.getProjects()));
	}

	private Widget parseProjects(ArrayList<Project> projects2) {
		VerticalPanel panel = new VerticalPanel();
		if(projects2.size()>0)
		panel.addStyleName("deck-Content");
		panel.setWidth("100%");
		for (final Project project : projects2) {
			HorizontalPanel p = new HorizontalPanel();
			p.sinkEvents(Event.ONCLICK);
			p.addHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					handler.openCloud(project);
				}
			},ClickEvent.getType());
			Widget w = ImageFactory.projectSmallIco();
			w.addStyleName("vertical-Middle");
			p.add(w);
			p.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_RIGHT);
			p.setCellWidth(w, "50px");
			p.add(new Label(project.getTitle()));
			panel.add(p);
			p.addStyleName("stackDeck-sideLink");
			p.addStyleName("hand");
		}

		WidgetFactory.glue(panel,"10px");
		return panel;
	}
	
	public void browseCategory(int cid){
		stackPanel.showStack(cid-1);
	}
	


}
