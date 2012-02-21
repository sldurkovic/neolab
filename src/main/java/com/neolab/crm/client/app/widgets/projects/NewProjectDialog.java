package com.neolab.crm.client.app.widgets.projects;

import gwtupload.client.IUploader;
import gwtupload.client.Uploader;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.events.NewProjectEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.client.mvp.view.ProjectsView;
import com.neolab.crm.shared.domain.Category;
import com.neolab.crm.shared.resources.Requires;
import com.neolab.crm.shared.resources.rpc.UploadType;

public class NewProjectDialog extends FormDialog implements Requires<Integer> {

	private String uploaderServletPath;
	private FlexTable table;
	private TextArea description;
	private TextBox title;
	private NewProjectHandler handler;
	private int category;
	private final ArrayList<Uploader> uploaders = new ArrayList<Uploader>();
	private Timer t;
	private ListBox lb;
	private ArrayList<Category> categories;

	public interface NewProjectHandler {
		void createProject(String title, int cid, String description,
				Requires<Integer> caller);
	}

	public NewProjectDialog(int category, NewProjectHandler handler) {
		this.category = category;
		table = new FlexTable();
		title = new TextBox();
		description = new TextArea();
		this.handler = handler;

		createUploader();
		createUploader();
		createUploader();

		renderPanel();
		center();
		setPopupPosition(Window.getClientWidth() / 2 - 300, 70);
		// setPopupPosition(200, 200);
		setWidth("450px");
		show();

		t = new Timer() {

			@Override
			public void run() {
				WidgetFactory.alert("Project created!");
				hide();
			}
		};
	}

	private void createUploader() {
		Uploader uploader = new Uploader(false);
		uploaderServletPath = uploader.getServletPath();
		uploader.setServletPath(uploaderServletPath + "?type="
				+ UploadType.PROJECT_DOCUMENTS.toString() + "&uid="
				+ Injector.INSTANCE.getApplication().getActiveUser().getUid());
		GWT.log(uploaderServletPath + "?type="
				+ UploadType.PROJECT_DOCUMENTS.toString() + "&uid="
				+ Injector.INSTANCE.getApplication().getActiveUser().getUid());
		uploaders.add(uploader);
	}

	private void renderPanel() {

		// header title
		Label header = new Label("Create new project");
		header.addStyleName("formDialog-Title");
		addWidget(header);


		
		TableWithCaption general = new TableWithCaption(null);
		final TextBox title = general.addTextBox("Project name: ",
				HasHorizontalAlignment.ALIGN_LEFT);
		// general.addDateBox("Start date: ");
		// general.addDateBox("End date: ");

		
		if(category == -1){
			lb = new ListBox();
			Injector.INSTANCE.getNeoService().getCategories(new AbstractAsyncCallback<ArrayList<Category>>() {
				@Override
				public void success(ArrayList<Category> result) {
					categories = result;
					for (Category category : result) {
						lb.addItem(category.getTitle());
					}
				}
			});
//			Iterator<Integer> iterator = ProjectsView.categories.keySet().iterator();
//			while (iterator.hasNext()){
//				int next = iterator.next();
//				lb.addItem(ProjectsView.categories.get(next));
//			}
			lb.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
//					category = lb.getSelectedIndex()+1;
					category = categories.get(lb.getSelectedIndex()).getCid();
				}
			});
			category = 1;
			general.addListBox("Category", lb, HasHorizontalAlignment.ALIGN_LEFT);
		}
		
		
		DisclosurePanel panel = new DisclosurePanel("Description");
		panel.setWidth("450px");
		final TextArea area = new TextArea();
		area.addStyleName("neo-TextBox");
		area.setWidth("400px");
		area.setHeight("60px");
		panel.add(area);
		panel.setOpen(true);
		general.addCustomWidget(panel, true);

		HorizontalPanel buttonBar = new HorizontalPanel();
		buttonBar.add(WidgetFactory.getBigButton("Create", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.createProject(title.getText(), category, area.getText(),
						NewProjectDialog.this);
				hide();
			}
		}));
		HTML glue = new HTML("&nbsp;");
		buttonBar.add(glue);
		buttonBar.setCellWidth(glue, "10px");
		buttonBar.add(WidgetFactory.getBigButton("Cancel", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// for(Uploader u : uploaders){
				// u.submit();
				// }
				hide();
			}
		}));

		// upload
		DisclosurePanel upload = new DisclosurePanel("Attach files");
		upload.setWidth("450px");
		// uploader = new MultiUploader();
		VerticalPanel u = new VerticalPanel();
		for (Uploader iu : uploaders) {
			u.add(iu);
		}
		upload.add(u);
		general.addCustomWidget(upload, true);

		general.addCustomWidget(buttonBar, false);
		addWidget(general);

		// TableWithCaption description = new TableWithCaption("Description");
		// description.addTextArea();
		// addWidget(description);

		// addWidget(panel);
	}

	private class TableWithCaption extends VerticalContainer {

		private FlexTable table;

		public TableWithCaption(String caption) {
			super(true);
			table = new FlexTable();
			table.setCellSpacing(5);
			table.getColumnFormatter().setWidth(0, "30%");
			table.getColumnFormatter().setWidth(1, "60%");
			if (caption != null) {
				Label title = new Label(caption);
				title.addStyleName("table-Caption");
				addWidget(title);
			}
			addWidget(table);
			getTopContainer().setCellHorizontalAlignment(table,
					HasHorizontalAlignment.ALIGN_RIGHT);
			addStyleName("table-With-Caption");
		}

		@Override
		protected void render() {
		}

		public TextBox addTextBox(String label,
				HasHorizontalAlignment.HorizontalAlignmentConstant align) {
			TextBox box = new TextBox();
			box.addStyleName("neo-TextBox");
			box.setWidth("200px");
			table.setWidget(table.getRowCount(), 0, new Label(label));
			table.getFlexCellFormatter().setAlignment(table.getRowCount() - 1,
					0, align, HasVerticalAlignment.ALIGN_MIDDLE);
			table.setWidget(table.getRowCount() - 1, 1, box);
			table.getFlexCellFormatter().setAlignment(table.getRowCount() - 1,
					1, HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
			return box;
		}
		
		public void addListBox(String label, ListBox lb,
				HasHorizontalAlignment.HorizontalAlignmentConstant align) {
			lb.setWidth("200px");
			table.setWidget(table.getRowCount(), 0, new Label(label));
			table.getFlexCellFormatter().setAlignment(table.getRowCount() - 1,
					0, align, HasVerticalAlignment.ALIGN_MIDDLE);
			table.setWidget(table.getRowCount() - 1, 1, lb);
			table.getFlexCellFormatter().setAlignment(table.getRowCount() - 1,
					1, HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
		}

		public TextArea addTextArea() {
			TextArea area = new TextArea();
			table.getFlexCellFormatter().setColSpan(table.getRowCount(), 0, 2);
			table.setWidget(table.getRowCount() - 1, 0, area);
			area.setWidth("350px");
			area.setHeight("100px");
			table.getFlexCellFormatter().setHorizontalAlignment(
					table.getRowCount() - 1, 0,
					HasHorizontalAlignment.ALIGN_CENTER);
			return area;
		}

		public DateBox addDateBox(String label) {
			DateBox box = new DateBox();
			DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
			box.setFormat(new DateBox.DefaultFormat(dateFormat));
			box.addStyleName("neo-DateBox");
			box.setWidth("100%");
			table.setWidget(table.getRowCount(), 0, new Label(label));
			table.getFlexCellFormatter().setAlignment(table.getRowCount() - 1,
					0, HasHorizontalAlignment.ALIGN_RIGHT,
					HasVerticalAlignment.ALIGN_MIDDLE);
			table.setWidget(table.getRowCount() - 1, 1, box);
			table.getFlexCellFormatter().setAlignment(table.getRowCount() - 1,
					1, HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
			return box;
		}

		public void addCustomWidget(Widget w, boolean span) {
			int index = table.getRowCount();
			if (!span) {
//				table.setWidget(index, 0, new HTML("&nbsp;"));
				index++;
			} else {
				table.getFlexCellFormatter().setColSpan(index, 0, 2);
			}
			if (span)
				table.setWidget(index, 0, w);
			else
				table.setWidget(index, 1, w);
			index++;
			table.getFlexCellFormatter().setAlignment(index - 1, 0,
					HasHorizontalAlignment.ALIGN_RIGHT,
					HasVerticalAlignment.ALIGN_MIDDLE);
		}

		@Override
		public void setWidth(String width) {
			super.setWidth(width);
			table.setWidth(width);
		}

	}

	@Override
	public void delivery(Integer object) {
		Injector.INSTANCE.getEventBus()
				.fireEvent(new NewProjectEvent(category));
		if (object != -1) {
			if (uploaders.size() > 0) {
				upload(uploaders.get(0), object);
				// hide();
			}

			// for (Uploader u : uploaders) {
			// u.setServletPath(u.getServletPath() + "&pid=" + object);
			// u.submit();
			// }
			// uploaders.clear();
		}

	}

	private void upload(Uploader u, final int object) {
		u.setServletPath(u.getServletPath() + "&pid=" + object);
		u.addOnStartUploadHandler(new IUploader.OnStartUploaderHandler() {

			@Override
			public void onStart(IUploader uploader) {
				t.cancel();
			}
		});
		u.addOnFinishUploadHandler(new Uploader.OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploader) {
				t.schedule(1000);
				uploaders.remove(0);
				if (uploaders.size() > 0) {
					upload(uploaders.get(0), object);
				}
			}
		});
		u.submit();
	}

}
