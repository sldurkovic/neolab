package com.neolab.crm.client.app.widgets.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle{
    public static Images INSTANCE = GWT.create(Images.class);

    @Source("retract.png")
    ImageResource retract();
    
    @Source("expand.png")
    ImageResource expand();
}
