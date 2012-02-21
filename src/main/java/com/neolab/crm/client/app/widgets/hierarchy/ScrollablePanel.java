package com.neolab.crm.client.app.widgets.hierarchy;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Dusan
 */
public class ScrollablePanel extends ScrollPanel {

    private final FlowPanel content;

    public ScrollablePanel() {
        setStyleName("scrollablePanel");
        content = new FlowPanel();
        content.setStyleName("scrollablePanel-Content");
        super.add(content);
    }

    public void add(Widget widget) {
        content.add(widget);
    }

    public void insert(Widget widget, int index) {
        content.insert(widget, index);
    }
}
