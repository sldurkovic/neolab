package com.neolab.crm.client.fwk;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CompositeToggleButton extends Composite implements HasValue<Boolean>, HasValueChangeHandlers<Boolean> {

	private HorizontalPanel holder;
	private com.google.gwt.user.client.ui.ToggleButton left;
	private com.google.gwt.user.client.ui.ToggleButton right;

	private boolean selected = false;

	public CompositeToggleButton(String leftText, String rightText, final boolean ask) {
		holder = new HorizontalPanel();
		holder.setStyleName("tae-Composite-ToggleButton");
		holder.addStyleName("no-Padding");
		initWidget(holder);
		left = new com.google.gwt.user.client.ui.ToggleButton(leftText);
		left.setStylePrimaryName("tae-Composite-ToggleButton-Left");
		holder.add(left);
		right = new com.google.gwt.user.client.ui.ToggleButton(rightText);
		right.setStylePrimaryName("tae-Composite-ToggleButton-Right");
		holder.add(right);
		left.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (ask){
				new ConfirmDialog(new ConfirmDialog.RequiresUserResponse() {
					
					@Override
					public void onResponse(boolean response) {
						if(response){
							setValue(true, true);
						}else{
							setValue(false, true);
						}
					}
				}, "Are you sure you want to change task status?", "YES", "NO");}else{
					setValue(true, true);
				}
			}
		});
		right.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (ask){
				new ConfirmDialog(new ConfirmDialog.RequiresUserResponse() {
					
					@Override
					public void onResponse(boolean response) {
						if(response){
							setValue(false, true);
						}else{
							setValue(true, true);
						}
					}
				}, "Are you sure you want to change task status?", "YES", "NO");	
				}else{setValue(false, true);}
			}
		});
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Boolean getValue() {
		return selected;
	}

	@Override
	public void setValue(Boolean value) {
		setValue(value, true);
	}

	@Override
	public void setValue(Boolean value, boolean fireEvents) {
		selected = value;
		if (selected) {
			left.setEnabled(false);
			left.setDown(true);
			right.setEnabled(true);
			right.setDown(false);
		} else {
			right.setEnabled(false);
			right.setDown(true);
			left.setEnabled(true);
			left.setDown(false);
		}
		if (fireEvents) {
			ValueChangeEvent.fire(this, selected);
		}
	}

}