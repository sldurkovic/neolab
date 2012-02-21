package com.neolab.crm.client.fwk;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.widgets.WaitPanel;
import com.neolab.crm.client.fwk.containers.VerticalContainer;
import com.neolab.crm.shared.resources.rpc.Response;

public class NeoForm extends VerticalContainer implements HasAsyncInformation{
	
	private FlexTable form;
	private HorizontalPanel infoLabel;

	public NeoForm() {
		super(true);
	}
	
	@Override
	protected void render() {
		form = new FlexTable();
		infoLabel = new HorizontalPanel();
		infoLabel.setSpacing(10);
		container.clear();
		container.add(form);
	}
	
	public void addField(String label, Widget textBox){
		form.setWidget(form.getRowCount(), 0, new Label(label));
		form.setWidget(form.getRowCount()-1, 1, textBox);
	}
	
	public void addSubmitButton(PushButton button){
		form.getFlexCellFormatter().setColSpan(form.getRowCount(), 0, 2);
		form.setWidget(form.getRowCount()-1, 0, infoLabel);

//		form.getFlexCellFormatter().setColSpan(form.getRowCount(), 0, 2);
		form.setWidget(form.getRowCount(), 1, button);
		
		form.getFlexCellFormatter().setHorizontalAlignment(form.getRowCount()-1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				WaitPanel.wait(true);
				infoLabel.add(ImageFactory.smallLoading());
				infoLabel.add(new Label("Processing request..."));
				infoLabel.addStyleName("green-Label");
			}
		});
	}

	@Override
	public void showInfo(Response response) {
		WaitPanel.wait(false);
		infoLabel.clear();
		infoLabel.add(new Label(response.getMsg()));
		if(response.getStatus()){
		}else{
			infoLabel.removeStyleName("green-Label");
			infoLabel.addStyleName("red-Label");
		}
	}

	public void addArea(TextArea body) {
		form.getFlexCellFormatter().setColSpan(form.getRowCount(), 0, 2);
		form.setWidget(form.getRowCount()-1, 0, body);
	}
	
}
