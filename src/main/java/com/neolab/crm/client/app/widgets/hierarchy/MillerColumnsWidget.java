package com.neolab.crm.client.app.widgets.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Dusan
 */
public class MillerColumnsWidget extends Composite {
    FlowPanel container = new FlowPanel();
    FlexTable wrapper;
    HeaderTable header;
    HeaderTable footer;
    FlexTable miller;
    BreadCrumbBar breadCrumbBar;
    Loading loadingWidget = new Loading();

    public MillerColumnsWidget() {
        wrapper = WidgetFactory.createFlexTable();
//        initWidget(wrapper);
        initWidget(container);
        DOM.setStyleAttribute(getElement(), "position", "relative");
        container.add(wrapper);
        container.add(loadingWidget);
        wrapper.setStyleName("millerColumns");
        breadCrumbBar = new BreadCrumbBar();
        header = new HeaderTable();
        footer = new HeaderTable();
        miller = WidgetFactory.createFlexTable();
        miller.setStylePrimaryName("millerColumns-columns");
        wrapper.setWidget(0, 0, breadCrumbBar);
        wrapper.setWidget(1, 0, header);
        wrapper.setWidget(2, 0, miller);
        wrapper.setWidget(3, 0, footer);
        wrapper.getFlexCellFormatter().setHeight(0, 0, "30px");
        wrapper.getFlexCellFormatter().setHeight(1, 0, Utils.isWebKit() ? "22px": "25px");
        wrapper.getFlexCellFormatter().setHeight(3, 0, "25px");
        miller.setHeight("100%");
        miller.setWidth("100%");
        DOM.setStyleAttribute(miller.getElement(), "tableLayout", "fixed");
        DOM.setStyleAttribute(header.getElement(), "tableLayout", "fixed");
        DOM.setStyleAttribute(footer.getElement(), "tableLayout", "fixed");
        DOM.setStyleAttribute(wrapper.getElement(), "tableLayout", "fixed");
        com.google.gwt.user.client.Element thead = getTHeadElement();
        DOM.sinkEvents(thead, Event.MOUSEEVENTS);
        DOM.setEventListener(thead, new ResizeListener(this));
    }

    public void addContent(int col, Widget widget) {
        miller.setWidget(0, col, widget);
        miller.getFlexCellFormatter().setAlignment(0, col, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
        miller.getFlexCellFormatter().setStyleName(0, col, "millerColumns-column");
        if (col > 0) miller.getFlexCellFormatter().addStyleName(0, col, "leftBorder");
    }

    public void addHeader(int col, String text) {
        header.setHeaderWidget(col, new Label(text));
    }

    public void addHeader(int col, Widget text) {
        header.setHeaderWidget(col, text);
    }

    public void addFooter(int col, String text){
        footer.setHeaderWidget(col, new Label());
    }

    public void addFooter(int col, Widget text) {
        footer.setHeaderWidget(col, text);
    }

    public int addColumn(String header) {
        int column = getLastColumn();
        addHeader(column, header);
        addContent(column, new Column());
        return column;
    }

    public int addColumn(Widget header) {
        int column = getLastColumn();
        addHeader(column, header);
        addContent(column, new Column());
        return column;
    }

    public int addColumn(Widget header, Widget footer) {
        int column = getLastColumn();
        addColumn(header);
        addFooter(column, footer);
        return column;
    }

    private int getLastColumn() {
        int column = 0;
        int rowCount = miller.getRowCount();
        if (rowCount > 0) {
            column = miller.getCellCount(0);
        }
        return column;
    }

    public void addRow(int col, Widget widget) {
        Column column = (Column) miller.getWidget(0, col);
        column.addRow(widget);
    }

    public void addRow(int col, Widget widget, boolean beforeLast) {
        Column column = (Column) miller.getWidget(0, col);
        column.addRow(widget, beforeLast);
    }

    public void removeColumn(int col) {
        header.removeHeaderWidget(col);
        footer.removeHeaderWidget(col);
        miller.removeCell(0, col);
    }

    public int getNumberOfColumns() {
        int rowCount = miller.getRowCount();
        if (rowCount > 0) {
            return miller.getCellCount(0);
        } else {
            return 0;
        }
    }

    public void removeWidget(int column, Widget w) {
        getColumn(column).deleteRow(w);
    }

    private Column getColumn(int column) {
        return (Column) miller.getWidget(0, column);
    }

    public com.google.gwt.user.client.Element getTHeadElement() {
//        return header.getRowFormatter().getElement(0);
        return header.getTHeadElement();
    }

    public void setColumnWidth(int column, int size) {
        GWT.log("setColumnSize: " + column + ", " + size, null);
        if (size < 0)
            size = 0;
        if (size < 0)
            size = 0;
        com.google.gwt.user.client.Element th = getTHElement(column, header);
        DOM.setStyleAttribute(th, "width", size + "px");
        com.google.gwt.user.client.Element thf = getTHElement(column, footer);
        DOM.setStyleAttribute(thf, "width", size + "px");
        if (miller.getRowCount() > 0) {
            HTMLTable.CellFormatter formatter = miller.getCellFormatter();
            com.google.gwt.user.client.Element td = formatter.getElement(0, column);
            DOM.setStyleAttribute(td, "width", size + "px");

            for (int i = 1; i < miller.getRowCount(); i++) {
                td = formatter.getElement(i, column);
                DOM.setStyleAttribute(td, "width", size + "px");
            }
        }
    }

    private com.google.gwt.user.client.Element getTHElement(int column, HeaderTable header) {
        com.google.gwt.user.client.Element tr = DOM.getChild(header.getTHeadElement(), 0);
        return DOM.getChild(tr, column);
    }

    public void clearColumn(int col){
        getColumn(col).clearWidgets();
    }

    private class Column extends FlowPanel implements ClickHandler {
        private FlexTable rows;
        private FlexTable.FlexCellFormatter formatter;
        int selectedRow = -1;
        Widget selectedWidget;

        private Column() {
            rows = WidgetFactory.createFlexTable();
            DOM.setStyleAttribute(rows.getElement(), "tableLayout", "fixed");
            formatter = rows.getFlexCellFormatter();
            rows.setWidth("100%");
//            rows.addClickHandler(this);
            super.add(rows);
        }

        public int addRow(Widget widget) {
            int index = rows.getRowCount();
            rows.setWidget(index, 0, widget);
            return index;
        }

        public int addRow(Widget widget, boolean beforeLast) {
            int index = rows.getRowCount();
            if (index > 2) {
                rows.insertRow(index - 1);
                rows.setWidget(index - 1, 0, widget);
                return index - 1;
            } else {
                rows.setWidget(index, 0, widget);
                return index;
            }
        }

        public void clearWidgets(){
            rows.removeAllRows();
        }

        public void deleteRow(int row) {
            if (rows.getWidget(row, 0) == selectedWidget) selectedWidget = null;
            rows.removeRow(row);
        }

        public void deleteRow(Widget widget) {
            deleteRow(getColumnWidgetIndex(widget));
        }

        private int getColumnWidgetIndex(Widget widget) {
            Element td = widget.getElement().getParentElement();
            Element tr = td.getParentElement();
            Element body = tr.getParentElement();
            return DOM.getChildIndex(((com.google.gwt.user.client.Element) body), ((com.google.gwt.user.client.Element) tr));
        }

        @Override
        public void onClick(ClickEvent event) {
            if (selectedWidget != null && selectedWidget instanceof Selectable) {
                ((Selectable) selectedWidget).deselect();
                for (int i = 0, j = rows.getRowCount(); i < j; i++) {
                    formatter.setStyleName(i, 0, "millerColumns-column-deselectedCell");
                }
            }

            HTMLTable.Cell clicked = rows.getCellForEvent(event);
            Widget target = rows.getWidget(clicked.getRowIndex(), clicked.getCellIndex());
            if (target instanceof Selectable) {
                selectedWidget = target;
                visuallySelectCell(clicked.getRowIndex());
                Selectable hasSelected = (Selectable) target;

                hasSelected.select();
            } else selectedWidget = null;
        }

         public void visuallySelectCell(int row) {
            formatter.setStyleName(row, 0, "millerColumns-column-selectedCell");
        }
    }

    private void setBorder(int row, int col) {
//        miller.getFlexCellFormatter().getElement(row, col).getStyle().setProperty("borderLeft", "2px solid gray");
    }

    public void setHeaderWidget(int column, Widget widget) {
        header.setHeaderWidget(column, widget);
    }

    public void setFooterWidget(int column, Widget widget) {
        footer.setHeaderWidget(column, widget);
    }

    public BreadCrumbBar getBreadCrumbBar() {
        return breadCrumbBar;
    }
    
    public void setElementId(String id) {
    	header.getElement().setId(id + "-header");
    	footer.getElement().setId(id + "-footer");
    	miller.getElement().setId(id + "-miller");
    	breadCrumbBar.setElementId(id + "-breadCrumbBar");
    }

    class Loading extends Composite{
        FlowPanel fp = new FlowPanel();

        private Loading() {
            initWidget(fp);
            setStyleName("millerColumns-loadingWidget");
            addStyleName("displayNone");

        }

        void show(){
            setWidth(MillerColumnsWidget.this.getElement().getOffsetWidth()+"px");
            setHeight(MillerColumnsWidget.this.getElement().getOffsetHeight()+"px");
            removeStyleName("displayNone");
        }

        void hide(){
            addStyleName("displayNone");
        }
    }
}
