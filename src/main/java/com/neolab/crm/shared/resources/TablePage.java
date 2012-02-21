package com.neolab.crm.shared.resources;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class TablePage<T> implements Serializable{
	
	private int total;
	private ArrayList<T> list;
	
	public TablePage() {
		list = new ArrayList<T>();
		total = 0;
	}
	
	public TablePage(int total, ArrayList<T> list) {
		this.list = list;
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<T> getList() {
		return list;
	}

	public void setList(ArrayList<T> list) {
		this.list = list;
	}
	
	public void addToList(T item){
		list.add(item);
	}

	@Override
	public String toString() {
		return "TablePage [total=" + total + ", list=" + list + "]";
	}
	
	

}
