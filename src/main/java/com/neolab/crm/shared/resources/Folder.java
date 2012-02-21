package com.neolab.crm.shared.resources;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Folder implements Serializable, HasLabel{
	
	private String label;
	
	public Folder() {
	}
	
	public Folder(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	
}
