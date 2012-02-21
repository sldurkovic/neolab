package com.neolab.crm.client.app.widgets.tables.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Resources;

public interface NeoCellListResources extends Resources{
    public Resources INSTANCE = GWT.create(NeoCellListResources.class);

	@Source("CellList.css")
	CellList.Style cellListStyle(); 
}
