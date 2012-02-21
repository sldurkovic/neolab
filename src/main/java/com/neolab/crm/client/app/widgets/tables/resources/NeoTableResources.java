package com.neolab.crm.client.app.widgets.tables.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
public interface NeoTableResources extends Resources{
    public NeoTableResources INSTANCE = GWT.create(NeoTableResources.class);

		@Source("CellTable.css")
		CellTable.Style cellTableStyle(); 
		
		@Source("loading.gif")
		ImageResource cellTableLoading();
}
