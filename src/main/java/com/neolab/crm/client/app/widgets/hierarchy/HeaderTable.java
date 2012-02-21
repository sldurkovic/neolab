package com.neolab.crm.client.app.widgets.hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Dusan
 */
public class HeaderTable extends FlexTable {
    private Element tHeadElement;
    /**
     * header widgets list
     */
    private List<Widget> headerWidgets = new ArrayList<Widget>();

    public HeaderTable() {
        setStylePrimaryName("millerColumns-header");
        setCellSpacing(0);
        setCellPadding(0);
        tHeadElement = DOM.createTHead();
        tHeadElement = DOM.createElement("thead");
        DOM.insertChild(getElement(), tHeadElement, 0);
        Element tr = DOM.createTR();
        DOM.insertChild(tHeadElement, tr, 0);
    }

    public void setHeaderWidget(int column, Widget widget) {
        prepareHeaderCell(column);

        if (widget != null) {
            try {
                widget.removeFromParent();
            } catch (Exception e) {
                
            }

            Element th = DOM.getChild(DOM.getFirstChild(tHeadElement), column);
            if (column > 0) DOM.setStyleAttribute(th, "borderLeft", "1px solid gray");

            internalClearCell(th, true);

            // Physical attach.
            DOM.appendChild(th, widget.getElement());

            if (headerWidgets.size() > column && headerWidgets.get(column) != null)
                headerWidgets.set(column, widget);
            else
                headerWidgets.add(column, widget);

            adopt(widget);
        }
    }

    public void removeHeaderWidget(int column) {
        if (column < 0)
            throw new IndexOutOfBoundsException("Column number mustn't be negative");
//        Widget w= headerWidgets.get(column);
//        w.removeFromParent();
        Element tr = DOM.getFirstChild(tHeadElement);
        Element th = DOM.getChild(tr, column);
        DOM.removeChild(tr, th);

//        headerWidgets.get(column).removeFromParent();
        headerWidgets.remove(column);
    }

    protected void prepareHeaderCell(int column) {
        if (column < 0) {
            throw new IndexOutOfBoundsException(
                    "Cannot create a column with a negative index: " + column
            );
        }

        if (tHeadElement == null) {
            tHeadElement = DOM.createElement("thead");
            DOM.insertChild(getElement(), tHeadElement, 0);
            Element tr = DOM.createTR();
            DOM.insertChild(tHeadElement, tr, 0);
        }

        if (headerWidgets.size() <= column || headerWidgets.get(column) == null) {
            int required = column + 1 - DOM.getChildCount(DOM.getChild(tHeadElement, 0));
            if (required > 0)
                addHeaderCells(tHeadElement, required);
        }
    }

    protected native void addHeaderCells (Element tHead, int num)/*-{
        var rowElem = tHead.rows[0];
        for(var i = 0; i < num; i++){
          var cell = $doc.createElement("th");
          rowElem.appendChild(cell);
        }
    }-*/;

    public Element getTHeadElement() {
        return tHeadElement;
    }
}
