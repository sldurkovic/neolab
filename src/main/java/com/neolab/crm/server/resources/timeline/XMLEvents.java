package com.neolab.crm.server.resources.timeline;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XML Data creator for Simile Timeline
 */
public class XMLEvents {
	
	private Document document;
	private Element root;
	
	public XMLEvents(){
		document = DocumentHelper.createDocument();
	    root = document.addElement("data");
	}
	
	public void addEvent(String start, String end, String title, String text){
		Event event = new Event(start, end);
		event.setTitle(title);
		event.setContent(text);
		addEvent(event);
	}
	
	public void addEvent(Event event){
		root.add(event.getElement());
	}
	
	public String getXml(){
		return document.asXML();
	}

	public class Event{
		
		private Element element;
		
		public Event(String start, String end){
			element = DocumentHelper.createElement("event");
			element.addAttribute("start", start);
	        element.addAttribute("end", end);
		}
		
		public void setTitle(String title){
			element.addAttribute("title", title);
		}
		
		public void setIcon(String icon){
			element.addAttribute("icon", icon);
		}
		
		public void setImage(String image){
			element.addAttribute("image", image);
		}
		
		public void setContent(String text){
			element.addText(text);
		}
		
		public void setIsDurations(boolean bool){
			element.addAttribute("isDuration", String.valueOf(bool));
		}
		
		public Element getElement(){
			return element;
		}
		
	}
	
}
