package com.neolab.crm.client.app.widgets;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.GoToPlaceEvent;
import com.neolab.crm.client.app.events.NewNewsEvent;
import com.neolab.crm.client.app.events.ProjectUserChangeEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.NewsStream.FeedProvider;
import com.neolab.crm.client.app.widgets.dialogs.AnnouncementDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.client.fwk.Labels;
import com.neolab.crm.client.fwk.containers.HorizontalContainer;
import com.neolab.crm.client.mvp.activities.HomeActivity;
import com.neolab.crm.client.mvp.places.ProjectsPlace;
import com.neolab.crm.shared.domain.News;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.CfgConstants;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.TablePage;
import com.neolab.crm.shared.resources.rpc.ObjectResponse;
import com.neolab.crm.shared.resources.rpc.Response;

public class NewsStream extends HorizontalContainer implements Requires<TablePage<News>>, HasAsyncInformation{

	public interface FeedProvider{
		void getFeed(int level, int start, int end, Requires<TablePage<News>> caller);
	}
	
	public interface ProjectsProvider{
		void getUserProjects(Requires<ArrayList<Project>> caller);
	}

	private VerticalPanel news;
	private int start;
	private int offset;
	private int total;
	private HomeActivity presenter;
	private RightSidebar sidebar;
	private FeedProvider provider;
	private static int PAGE_SIZE = 3;
	
	public NewsStream(FeedProvider provider) {
		super(false);
		this.provider = provider;
		news = new VerticalPanel();
		news.addStyleName("news-Container");
		news.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		render();
		getNews(0, 0, PAGE_SIZE);
		Injector.INSTANCE.getEventBus().addHandler(NewNewsEvent.TYPE, new NewNewsEvent.Handler<NewNewsEvent>() {
			@Override
			public void on(NewNewsEvent e) {
				getNews(0, 0, PAGE_SIZE);
		}});
		
	}

	@Override
	protected void render() {
		addWidget(news);
//		sidebar = new RightSidebar(presenter);
//		addWidget(sidebar);
		getTopContainer().setCellWidth(news, "100%");
		getTopContainer().setWidth("100%");
	}
	
	private void renderNews(ArrayList<News> object){
		news.clear();
		if(object.size() == 0)
			news.add(Labels.getItalicLabel("No new announcements"));
		for (News n : object) {
			FlexTable table = new FlexTable();
			table.setCellPadding(0);
			table.setCellSpacing(0);
			table.setStyleName("news-Table");
			final User user = n.getUser();
			Label author = new Label(n.getUser().getFirstName()+" "+n.getUser().getLastName());
			author.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					new UserPreviewWidget(user);
				}
			});
			
			author.addStyleName("news-Author");
			author.addStyleName("hand");
			
			Label date = new Label(CfgConstants.DATE_TIME_FORMAT_AT.format(n.getDate())+" by ");
			date.addStyleName("news-Date");
			
			HorizontalPanel authorDate = new HorizontalPanel();
			authorDate.add(date);
			WidgetFactory.glue(authorDate);
			authorDate.add(author);
			authorDate.setCellHorizontalAlignment(author, HasHorizontalAlignment.ALIGN_RIGHT);
			
			Label title = new Label(n.getTitle());
			HTML body = new HTML(n.getBody());

			table.setWidget(0, 0, title);
			table.getFlexCellFormatter().setStyleName(0, 0, "news-Title");
			table.setWidget(0, 1, authorDate);
			table.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
				table.getFlexCellFormatter().setStyleName(2, 0, "news-Body");
				table.setWidget(2, 0, body);
				table.getFlexCellFormatter().setColSpan(2, 0, 2);
			
			news.add(table);
			news.setWidth("100%");
		}
		constructBar();
		
	}
	
	private void constructBar(){
		if(start+offset > total && start-offset < 0)
			return;
		HorizontalPanel bar = new HorizontalPanel();
		bar.addStyleName("news-Bar");
		HorizontalPanel older = new HorizontalPanel();
		older.add(ImageFactory.retract());
		WidgetFactory.glue(older);
		older.add(new Label("Older"));
		older.addStyleName("hand");
		older.addStyleName("paginator");
		WidgetFactory.addPanelClick(older, new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getNews(0, start+PAGE_SIZE, PAGE_SIZE);
			}
		});
		HorizontalPanel newer = new HorizontalPanel();
		newer.add(new Label("Newer"));
		WidgetFactory.glue(newer);
		newer.add(ImageFactory.expand());
		newer.addStyleName("hand");
		newer.addStyleName("paginator");
		WidgetFactory.addPanelClick(newer, new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getNews(0, start-PAGE_SIZE, PAGE_SIZE);
			}
		});
		if(start+offset <= total)
		bar.add(older);
//		WidgetFactory.glue(bar, "20px", "|");
		int n = start-PAGE_SIZE;
		if(n >= 0 && n <= total)
			bar.add(newer);
		GWT.log(n+"");
		bar.setCellHorizontalAlignment(newer, HasHorizontalAlignment.ALIGN_RIGHT);
		bar.setWidth("100%");
		
		news.add(bar);
	}
	
	private void getNews(int level, int start, int end){
		this.start = start;
		this.offset = end;
		news.clear();
		Image image = ImageFactory.smallLoading();
		news.add(image);
		news.setCellHorizontalAlignment(image, HasHorizontalAlignment.ALIGN_CENTER);
		news.setWidth("100%");
		provider.getFeed(level, start, end, this);
	}

	@Override
	public void delivery(TablePage<News> result) {
		total = result.getTotal();
		news.setWidth("");
		renderNews(result.getList());
	}

	@Override
	public void showInfo(Response response) {
		WidgetFactory.info(response.getMsg());
	}

}
