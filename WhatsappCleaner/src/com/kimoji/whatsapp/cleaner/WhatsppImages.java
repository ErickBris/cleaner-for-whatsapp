package com.kimoji.whatsapp.cleaner;

import java.io.Serializable;

public class WhatsppImages implements Serializable{

	String path="";
	String name="";
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	boolean selected = false;
	long size=0;
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
