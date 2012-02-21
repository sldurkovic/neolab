package com.neolab.crm.client.mvp.view;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.widgets.StackSideDeck;
import com.neolab.crm.client.app.widgets.dialogs.NewCategoryDialog;
import com.neolab.crm.client.app.widgets.projects.CategoryExplorer;
import com.neolab.crm.client.app.widgets.projects.UserCategoryProjects;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.mvp.BaseView;
import com.neolab.crm.client.mvp.activities.ProjectsActivity;
import com.neolab.crm.shared.domain.Category;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.resources.ProjectPage;
import com.neolab.crm.shared.resources.Requires;

public class ProjectsView extends BaseView<ProjectsActivity> implements Requires<ArrayList<Category>>{

	public interface UserCategoryProjectsProvider{
		void getUserCategoryProjects(int cid, Requires<UserCategoryProjects> caller);
		void openCloud(Project project);
	}
	
	
	
	private HorizontalPanel container;
	private StackSideDeck deck;
//	private AsyncProvider<ProjectPage> projectProvider;
//	public static HashMap<Integer, String> categories;
//	static{
//		categories = new HashMap<Integer, String>();
//		categories.put(0, "General projects");
//		categories.put(1, "Semantic web");
//		categories.put(2, "GWT");
//	}
	
	private ArrayList<Category> categories;
	
	public ProjectsView(ProjectsActivity p) {
		super(p);
//		projectProvider = new AsyncProvider<ProjectPage>(){
//			@Override
//			public void getObject(int id, Requires<ProjectPage> caller) {
//				presenter.getProjectById(id, caller);
//			}
//		};
		p.getCategories(this);
	}

	@Override
	public void render() {
		container = new HorizontalPanel();
		getTopContainer().add(container);
		getTopContainer().setCellWidth(container, "100%");
		container.setWidth("100%");
//		getTopContainer().setCellHorizontalAlignment(container, HasHorizontalAlignment.ALIGN_CENTER);
//		getTopContainer().setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		getTopContainer().setWidth("100%");
		deck = new StackSideDeck(new UserCategoryProjectsProvider() {
			
			@Override
			public void getUserCategoryProjects(int cid,
					Requires<UserCategoryProjects> caller) {
				presenter.getUserCategoryProjects(cid, caller);
			}
			@Override
			public void openCloud(Project project) {
				deck.getVisibleExplorer().openCloud(project);
			}
		}, new NewCategoryDialog.CategoryProvider() {
			
			@Override
			public void createCategory(String title, HasAsyncInformation caller) {
				presenter.createCategory(title, caller);
			}
		});
		deck.addStyleName("projects-Deck");
		for (Category cat : categories) {
			deck.addView(cat, new CategoryExplorer(cat.getCid(), presenter));
		}
		container.add(deck);
		deck.setWidth("100%");
		container.setCellWidth(deck, "100%");
	}
	
	public int getSelectedCategory(){
		return deck.getSelectedIndex();
	}
	
	@Override
	public void addWidget(Widget w) {
		container.add(w);
	}
	
	public void openProject(int pid){
		presenter.getProjectById(pid, new Requires<ProjectPage>() {
			@Override
			public void delivery(ProjectPage object) {
				deck.browseCategory(object.getProject().getCid());
				deck.getCategoryExplorer(object.getProject().getCid()).openCloud(object.getProject());
			}
		});
	}

	public void delivery(ArrayList<Category> object) {
		categories = object;
		render();
	}
	
}
