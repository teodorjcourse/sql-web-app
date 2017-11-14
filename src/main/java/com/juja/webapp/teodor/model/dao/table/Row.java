package com.juja.webapp.teodor.model.dao.table;

import java.util.List;

public class Row {
	private List<Cell> row;

	public Row(List<Cell> row) {
		this.row = row;
	}

	public List<Cell> cells() {
		return this.row;
	}
}
