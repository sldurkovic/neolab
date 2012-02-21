package com.neolab.crm.client.app.widgets;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.fwk.containers.FlowContainer;

public class PropertyTable extends FlowContainer{

	private FlexTable table;
	
	private boolean light;
	
	public PropertyTable() {
		super(true);
	}

	@Override
	protected void render() {
		table = new FlexTable();
		table.addStyleName("property-Table");
		table.setCellSpacing(0);
		addWidget(table);
	}
	
	public void insertWidget(Widget key, Widget value){
		int row = table.getRowCount();
		table.setWidget(row, 0, key);
		table.setWidget(row, 1, key);
		if(light)
			table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Light");
		table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key");
		table.getFlexCellFormatter().addStyleName(row, 1, "property-Table-Value");
	}
	
	public void insertWidget(String key, String value){
		int row = table.getRowCount();
		table.setWidget(row, 0, new Label(key));
		table.setWidget(row, 1, new Label(value));
		if(light)
			table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key-Light");
		table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key");
		table.getFlexCellFormatter().addStyleName(row, 1, "property-Table-Value");
	}
	
	public void insertWidget(String key, Widget value){
		int row = table.getRowCount();
		table.setWidget(row, 0, new Label(key));
		table.setWidget(row, 1, value);
		if(light)
			table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key-Light");
		table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key");
		table.getFlexCellFormatter().addStyleName(row, 1, "property-Table-Value");
	}
	
	
	public TextBox insertTextWidget(String key, String width){
		int row = table.getRowCount();
		TextBox box = new TextBox();
		if(width != null)
			box.setWidth(width);
		box.addStyleName("neo-TextBox");
		table.setWidget(row, 0, new Label(key));
		table.setWidget(row, 1, box);
		if(light)
			table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key-Light");
		table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key");
		table.getFlexCellFormatter().addStyleName(row, 1, "property-Table-Value");
		return box;
	}
	
	public PasswordTextBox insertPasswordWidget(String key){
		int row = table.getRowCount();
		PasswordTextBox box = new PasswordTextBox();
		box.addStyleName("neo-TextBox");
		table.setWidget(row, 0, new Label(key));
		table.setWidget(row, 1, box);
		if(light)
			table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key-Light");
		table.getFlexCellFormatter().addStyleName(row, 0, "property-Table-Key");
		table.getFlexCellFormatter().addStyleName(row, 1, "property-Table-Value");
		return box;
	}
	
	public void insertButton(Widget p){
		int row = table.getRowCount();
		table.setWidget(row, 1, p);
	}
	
	@Override
	public void setWidth(String width) {
		table.setWidth(width);
	}
	
	
	@Override
	public void setHeight(String height) {
		table.setHeight(height);
	}
	
	public void setLightKeyColor(){
		light = true;
	}

}
