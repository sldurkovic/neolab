package com.neolab.crm.client.app.widgets.tables.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
public interface NeoPagerResources extends Resources{
    public NeoPagerResources INSTANCE = GWT.create(NeoPagerResources.class);
    

    @Source("expand.png")
    ImageResource simplePagerNextPage();
    
    @Source("expand_inactive.png")
    ImageResource simplePagerNextPageDisabled();

    @Source("retract.png")
    ImageResource simplePagerPreviousPage();
    
    @Source("retract_inactive.png")
    ImageResource simplePagerPreviousPageDisabled();
    
    @Source("first_page.png")
    ImageResource simplePagerFirstPage();
    
    @Source("first_page_inactive.png")
    ImageResource simplePagerFirstPageDisabled();
    

    @Source("last_page.png")
    ImageResource simplePagerLastPage();
    
    @Source("last_page_inactive.png")
    ImageResource simplePagerLastPageDisabled();
}
