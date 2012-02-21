package com.neolab.crm.client.app.widgets.hierarchy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * @author Dusan
 */
public class BreadCrumbBar<E> extends Composite {

    interface Resources extends ClientBundle {
        ImageResource previous();
        ImageResource next();
        ImageResource retract();
        ImageResource expand();
    }
    static Resources resources = GWT.create(Resources.class);

    List<E> elements = new LinkedList<E>();
    AbsolutePanel holder = new AbsolutePanel();
    HorizontalPanel wrapper = new HorizontalPanel();
    private int wrapperWith;
    private int wrapperLeft;
    private int with;
    private NavButton up = new NavButton(new Image(Images.INSTANCE.retract()), new Image(Images.INSTANCE.retract()));
    private NavButton down = new NavButton(new Image(Images.INSTANCE.expand()), new Image(Images.INSTANCE.expand()));
    private SimpleMillerColumns simpleMillerColumns;
    private AnimationNext animationNext = new AnimationNext();
    private AnimationPrevious animationPrevious = new AnimationPrevious();

    public BreadCrumbBar() {
        AbsolutePanel container = new AbsolutePanel();
        wrapper = new HorizontalPanel();
        initWidget(container);
        setStylePrimaryName("millerColumns-breadCrumbBar");
        up = new NavButton(new Image(resources.retract()), new Image(resources.retract()));
        down = new NavButton(new Image(resources.expand()), new Image(resources.expand()));
        container.add(up, 0, 5);
        holder.add(wrapper, 0, 0);
        container.add(holder);
        container.add(down);
        DOM.setStyleAttribute(holder.getElement(), "position", "absolute");
        DOM.setStyleAttribute(holder.getElement(), "right", "20px");
        DOM.setStyleAttribute(holder.getElement(), "left", "20px");
        DOM.setStyleAttribute(down.getElement(), "position", "absolute");
        DOM.setStyleAttribute(down.getElement(), "right", "0px");
        DOM.setStyleAttribute(down.getElement(), "top", "2px");
        DOM.setStyleAttribute(up.getElement(), "top", "2px");
        DOM.setStyleAttribute(holder.getElement(), "marginBottom", "2px");
        holder.setHeight("20px");
        up.setDown(false);
        down.setDown(false);

        up.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                previous();
            }
        });

        up.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                stopPreviouse();
            }
        });

        down.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                next();
            }
        });

        down.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                stopNext();
            }
        });
    }

    public void add(E element) {
        String title = simpleMillerColumns.getElementHeaderString(element);
        boolean isBold = simpleMillerColumns.isElementHeaderBold(element);
        elements.add(element);
        final BarLabel l = new BarLabel(title, elements.size() - 1, this, isBold);
        wrapper.add(l);
        setUpPosition();
    }

    public void changeLastElement(E element) {
        elements.set(elements.size() - 1, element);
        String title = simpleMillerColumns.getElementHeaderString(element);
        boolean isBold = simpleMillerColumns.isElementHeaderBold(element);
        BarLabel label = (BarLabel) wrapper.getWidget(wrapper.getWidgetCount() - 1);
        label.setElementText(title);
        label.setBold(isBold);
        setUpPosition();
    }


    private void show(int index) {
        if (index >= elements.size() - 1) return;
        E elem0 = null;
        E elem = elements.get(index);
        if (index > 0) elem0 = elements.get(index - 1);
        List<E> newElements = new ArrayList<E>();
        newElements.addAll(elements.subList(0, index + 1));
        elements = newElements;
        while (wrapper.getWidgetCount() > index + 1) {
            wrapper.remove(wrapper.getWidgetCount() - 1);
        }
        setUpPosition();
        simpleMillerColumns.changeLevel(index + 1, elem0, elem);
    }

    public void reset() {
        elements = new ArrayList<E>();
        while (wrapper.getWidgetCount() > 0) {
            wrapper.remove(wrapper.getWidgetCount() - 1);
        }
        setUpPosition();
    }


    private void setUpPosition() {
        if (wrapper.getParent() == holder) {
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    enableScroller();
                    if (wrapperWith > with) {
                        int oldWrapperLeft = wrapperLeft;
                        wrapperLeft = with - wrapperWith;
//                        int move;
//                        if (oldWrapperLeft < 0)
//                            move = wrapperLeft - oldWrapperLeft;
//                        Log.debug("move: " + move);
//                        if (move > 0) animationPrevious.run();
//                        else next();
                        holder.setWidgetPosition(wrapper, wrapperLeft, 0);
                    } else if (wrapperLeft < 0) {
//                        Log.debug("move2: " + (-wrapperLeft));
                        holder.setWidgetPosition(wrapper, 0, 0);
//                        previous();
                    }
                }
            });
        } else DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                setUpPosition();
            }
        });

    }

    private void calcWiths() {
        wrapperWith = wrapper.getOffsetWidth();
        with = holder.getOffsetWidth();
        wrapperLeft = wrapper.getAbsoluteLeft() - holder.getAbsoluteLeft();
//        Log.debug("with: " + with);
//        Log.debug("wrapperWith: " + wrapperWith);
//        Log.debug("wrapperLeft: " + wrapperLeft);
    }

    private void previous() {
        animationPrevious.cancel();
        calcWiths();
        if (wrapperLeft < 0) {
            final int move = -wrapperLeft;
            animationPrevious.run(6 * move, move);
        }
    }

    private void stopPreviouse() {
        animationPrevious.cancel();
    }

    private void next() {
        animationNext.cancel();
        calcWiths();
        if (wrapperWith > with && wrapperLeft + wrapperWith > with) {
            final int move = (wrapperWith + wrapperLeft) - with;
            animationNext.run(6 * move, move);
        }
    }

    private void stopNext() {
        animationNext.cancel();
    }

    private void enableScroller() {
        calcWiths();
        if (wrapperWith > with) {
            DOM.setStyleAttribute(up.getElement(), "visibility", "visible");
            DOM.setStyleAttribute(down.getElement(), "visibility", "visible");
            DOM.setStyleAttribute(holder.getElement(), "right", "28px");
            DOM.setStyleAttribute(holder.getElement(), "left", "28px");
        } else {
            DOM.setStyleAttribute(up.getElement(), "visibility", "hidden");
            DOM.setStyleAttribute(down.getElement(), "visibility", "hidden");
            DOM.setStyleAttribute(holder.getElement(), "right", "3px");
            DOM.setStyleAttribute(holder.getElement(), "left", "3px");
        }
        calcWiths();
    }

    public int getElementsSize() {
        return elements.size();
    }

    private static class BarLabel extends Composite implements ClickHandler {
        private int index;
        private BreadCrumbBar bar;
        private FlowPanel fp;
        private InlineLabel label;
        private InlineLabel separator;

        private BarLabel(String text, int index, BreadCrumbBar bar, boolean isBold) {
            this.index = index;
            this.bar = bar;

            label = new InlineLabel();
            separator = new InlineLabel();
            separator.setText(">");
            DOM.setStyleAttribute(separator.getElement(), "marginRight", "3px");
            DOM.setStyleAttribute(separator.getElement(), "height", "20px");
            DOM.setStyleAttribute(separator.getElement(), "lineHeight", "20px");
            DOM.setStyleAttribute(label.getElement(), "marginRight", "3px");
            DOM.setStyleAttribute(label.getElement(), "height", "20px");
            DOM.setStyleAttribute(label.getElement(), "lineHeight", "20px");
            label.addClickHandler(this);

            fp = new FlowPanel();
            initWidget(fp);
            fp.add(separator);
            fp.add(label);

            setElementText(text);
            label.addStyleName("hand");
            setBold(isBold);
        }

        public void setBold(boolean isBold) {
        	if(isBold) {
        		label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
        	} else {
        		label.getElement().getStyle().setFontWeight(FontWeight.NORMAL);
        	}
		}

		@Override
        public void onClick(ClickEvent event) {
            bar.show(index);
        }

        public void setElementText(String text) {
            if (index > 0) {
                if (fp.getWidget(0) != separator) fp.insert(separator, 0);
            } else {
                if (fp.getWidget(0) == separator) fp.remove(separator);
            }
            label.setText(text);
        }
    }

    public void setSimpleMillerColumns(SimpleMillerColumns simpleMillerColumns) {
        this.simpleMillerColumns = simpleMillerColumns;
    }

    private class AnimationNext extends Animation {
        private int move;

        public void run(int duration, int move) {
            this.move = move;
            up.setDown(true);
            super.run(duration);
        }

        @Override
        protected void onUpdate(double progress) {
            int toMove = (int) Math.round(progress * move);
            holder.setWidgetPosition(wrapper, wrapperLeft - toMove, 0);
        }

        @Override
        protected void onComplete() {
            calcWiths();
            up.setDown(false);
        }
    }

    private class AnimationPrevious extends Animation {
        private int move;

        public void run(int duration, int move) {
            this.move = move;
            down.setDown(true);
            super.run(duration);
        }

        @Override
        protected void onUpdate(double progress) {
            int toMove = (int) Math.round(progress * move);
            holder.setWidgetPosition(wrapper, wrapperLeft + toMove, 0);
        }

        @Override
        protected void onComplete() {
            calcWiths();
            down.setDown(false);
        }
    }

    E getElement(int index) {
        return elements.get(index);
    }

    public void setElementId(String id) {
        getElement().setId(id);
        up.getElement().setId(id + "-up");
        down.getElement().setId(id + "-down");
    }
}
