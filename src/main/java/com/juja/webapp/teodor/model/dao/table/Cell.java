package com.juja.webapp.teodor.model.dao.table;

public class Cell {
	private String key;
	private Object value;

	public Cell(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String key() {
		return this.key;
	}

	public Object value() {
		return this.value;
	}
}
