package com.neolab.crm.client.app.widgets.dialogs;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.neolab.crm.client.app.events.NewNewsEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.RichTextToolbar;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.shared.resources.rpc.Response;

public class AnnouncementDialog extends FormDialog implements HasAsyncInformation{

	private int pid;
	private CreateNewsProvider provider;
	private TextBox title;
	public interface CreateNewsProvider{
		public void createNews(String title, String body, int level, int pid,
				HasAsyncInformation caller);
	}
	
	public AnnouncementDialog(int pid, CreateNewsProvider provider) {
		this.provider = provider;
		this.pid = pid;
		renderPanel();
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}

	private void renderPanel() {
		
		Label head = new Label("Write news");
		head.addStyleName("formDialog-Title");
		addWidget(head);
		
		final ListBox list = new ListBox();
		list.addItem("All");
		
		ArrayList<String> levels = Injector.INSTANCE.getApplication().getLevels();
		
		for (String string : levels) {
			list.addItem(string);
		}
		
		title = new TextBox();
		
		FlexTable table = new FlexTable();
		table.setWidget(1, 0, new Label("Level: "));
		table.setWidget(1, 1, list);
		table.setWidget(0, 0, new Label("Title: "));
		table.setWidget(0, 1, title);

		table.setWidget(2, 0, new HTML("&nbsp;"));
		addWidget(table);
		getContent().setCellHorizontalAlignment(table, HasHorizontalAlignment.ALIGN_LEFT);


//		HorizontalPanel listPanel = new HorizontalPanel();
//		listPanel.add(new Label("Level: "));
//		WidgetFactory.glue(listPanel);
//		
//		listPanel.add(list);
//		addWidget(listPanel);
//		getContent().setCellHeight(listPanel, "30px");
//		getContent().setCellHorizontalAlignment(listPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		
		
		 // Create the text area and toolbar
	    final RichTextArea area = new RichTextArea();
	    area.ensureDebugId("cwRichText-area");
	    area.setSize("100%", "14em");
	    RichTextToolbar toolbar = new RichTextToolbar(area);
	    toolbar.ensureDebugId("cwRichText-toolbar");
	    toolbar.setWidth("100%");

	    // Add the components to a panel
	    Grid grid = new Grid(2, 1);
	    grid.setStyleName("cw-RichText");
	    grid.setWidget(0, 0, toolbar);
	    grid.setWidget(1, 0, area);
	    addWidget(grid);
	    addWidget(WidgetFactory.getBigButton("Send", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				provider.createNews(title.getText(), area.getHTML(), list.getSelectedIndex(), pid, AnnouncementDialog.this);
			}
		}));
	}

	@Override
	public void showInfo(Response response) {
		WidgetFactory.notify(response);
		if(response.getStatus()){
			hide();
			Injector.INSTANCE.getEventBus().fireEvent(new NewNewsEvent());
		}
	}
	
}
