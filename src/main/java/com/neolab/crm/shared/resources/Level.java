package com.neolab.crm.shared.resources;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Level implements Serializable{
	
	private String name;
	private ArrayList<String> options;
	
	public Level() {
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}
	
	public boolean canDo(Option option){
		if(options != null){
			for(String o : options){
				if(o.equals(option.toString()))
					return true;
			}
		}
		return false;
	}
	
}
