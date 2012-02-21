package com.neolab.crm.shared.resources;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class ColumnSort implements Serializable {
	
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String EMAIL = "email";
	public static final String TITLE = "title";
	public static final String PASSWORD = "password";
	public static final String DATE_CREATED = "dateCreated";
	public static final String DATE_START = "dateStart";
	public static final String DATE_END = "dateEnd";
	public static final String TASK_STATUS = "status";
	public static final String LEVEL = "level";
	
	public static HashMap<String, String> tableMapping;
	static{
		tableMapping = new HashMap<String, String>();
		tableMapping.put(DATE_START, "date_start");
		tableMapping.put(DATE_CREATED, "date_created");
		tableMapping.put(DATE_END, "date_end");
		tableMapping.put(FIRST_NAME, "first_name");
		tableMapping.put(LAST_NAME, "last_name");
	}
	
	private String column;
	private boolean ascending;
	
	public ColumnSort() {
	}

	public ColumnSort(String column, boolean ascending) {
		this.column = column;
		this.ascending = ascending;
	}

	public String getColumn() {
		return column;
	}

	public boolean isAscending() {
		return ascending;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ascending ? 1231 : 1237);
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnSort other = (ColumnSort) obj;
		if (ascending != other.ascending)
			return false;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		return true;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
	
	public String toSqlString(){
		if(ascending)
			return "ORDER BY "+((tableMapping.get(column)!=null)? tableMapping.get(column): column)+" ASC";
		else 
			return "ORDER BY "+((tableMapping.get(column)!=null)? tableMapping.get(column): column)+" DESC";
	}

	@Override
	public String toString() {
		return "ColumnSort [column=" + column + ", ascending=" + ascending
				+ "]";
	}
	

}
