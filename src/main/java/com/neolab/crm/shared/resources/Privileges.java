package com.neolab.crm.shared.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;

@SuppressWarnings("serial")
public class Privileges implements Serializable {

	private HashMap<Integer, Level> levels;

	public Privileges() {
	}

	public HashMap<Integer, Level> getLevels() {
		return levels;
	}

	public void setLevels(HashMap<Integer, Level> levels) {
		this.levels = levels;
	}

	public boolean authorize(int level, Option option) {
		if (levels != null) {
			return levels.get(level).canDo(option);
		}
		return false;
	}

	public String getName(int level) {
		if(level != 0)
		return levels.get(level).getName();
		else
			return "";
	}
	
	public int getLevel(String level) {
		Iterator<Integer> iterator = levels.keySet().iterator();
		while (iterator.hasNext()) {
			int next = iterator.next();
			if(levels.get(next).getName().equals(level))
				return next;
		}
		return 0;
	}

	public ArrayList<String> getStringLevels() {
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Integer> iterator = levels.keySet().iterator();
		while (iterator.hasNext()) {
			list.add(levels.get(iterator.next()).getName());
		}
		return list;
	}

}
