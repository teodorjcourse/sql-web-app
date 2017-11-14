package com.juja.webapp.teodor.model.dao.table;

import java.util.List;

public class Table {
	private List<Column> columns;
	private List<Row> rows;

	public Table(List<Column> columns, List<Row> rows) {
		this.columns = columns;
		this.rows = rows;
	}

	public List<Column> columns() {
		return this.columns;
	}

	public List<Row> rows() {
		return this.rows;
	}

	public Row row(int index) {
		return rows.get(index);
	}

	public Column column(int index) {
		return columns.get(index);
	}

	public Column column(String name) {
		for (Column c : columns) {
			if (c.name().equals(name)) {
				return c;
			}
		}

		return null;
	}
}


