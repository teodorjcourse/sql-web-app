package com.juja.webapp.teodor.model.dao.table;

public class Column {
	private int index;
	private String name;

	public Column(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String name() {
		return this.name;
	}

	public int index() {
		return this.index;
	}
}
