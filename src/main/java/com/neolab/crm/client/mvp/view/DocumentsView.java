package com.neolab.crm.client.mvp.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.widgets.hierarchy.FolderMillerColumnModel;
import com.neolab.crm.client.app.widgets.hierarchy.SimpleMillerColumns;
import com.neolab.crm.client.mvp.BaseView;
import com.neolab.crm.client.mvp.activities.DocumentsActivity;
import com.neolab.crm.shared.resources.Folder;

public class DocumentsView extends BaseView<DocumentsActivity>{
	
	private HorizontalPanel container;
	
	public DocumentsView(DocumentsActivity p){
		super(p);
		render();
	}

	@Override
	public void render() {
		container = new HorizontalPanel();
		getTopContainer().add(container);
		SimpleMillerColumns<Folder> hierarchyMillerColumns = new SimpleMillerColumns<Folder>();
		hierarchyMillerColumns.setRoot(FolderMillerColumnModel.ROOT);
		hierarchyMillerColumns.setModel(new FolderMillerColumnModel());

		hierarchyMillerColumns.addValueChangeHandler(new ValueChangeHandler<Folder>() {
			@Override
			public void onValueChange(ValueChangeEvent<Folder> event) {
			}
		});
		hierarchyMillerColumns.setWidth("500px");
		hierarchyMillerColumns.refresh();
		addWidget(hierarchyMillerColumns);
	}
	
	@Override
	public void addWidget(Widget w) {
		container.add(w);
	}
	
}
