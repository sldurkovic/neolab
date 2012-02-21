package com.neolab.crm.client.app.widgets.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.dialogs.NewCategoryDialog.CategoryProvider;
import com.neolab.crm.client.fwk.FormDialog;
import com.neolab.crm.client.fwk.HasAsyncInformation;
import com.neolab.crm.shared.resources.rpc.Response;

public class NewCategoryDialog extends FormDialog implements HasAsyncInformation {

	public interface CategoryProvider{
		void createCategory(String title, HasAsyncInformation caller);
	}

	private CategoryProvider provider;
	
	public NewCategoryDialog(CategoryProvider provider) {
		this.provider = provider;
		renderPanel();
	    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(left, 70);
	}
	
	private void renderPanel() {
		final TextBox box = new TextBox();
		box.setWidth("200px");
		Label label = new Label("Title: ");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(label);
		WidgetFactory.glue(hp);
		hp.add(box);
		addWidget(hp);
		
		addButton("Create", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				provider.createCategory(box.getText(), NewCategoryDialog.this);
			}
		});
		
		addButton("Cancel", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
	}

	@Override
	public void showInfo(Response response) {
		WidgetFactory.notify(response);
	}


}
