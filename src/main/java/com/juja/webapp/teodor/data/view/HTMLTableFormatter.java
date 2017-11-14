package com.juja.webapp.teodor.data.view;

import com.juja.webapp.teodor.model.dao.table.Cell;
import com.juja.webapp.teodor.model.dao.table.Column;
import com.juja.webapp.teodor.model.dao.table.Row;
import com.juja.webapp.teodor.model.dao.table.Table;

import java.util.List;

public class HTMLTableFormatter {
	private Table table;
	private String keyField;

	public HTMLTableFormatter(Table table) {
		this(table, "uid");
	}

	public HTMLTableFormatter(Table table, String keyField) {
		this.table = table;
		this.keyField = keyField;
	}

	public StringBuilder tHead() {
		return tHead(new StringBuilder());
	}

	public StringBuilder tHead(StringBuilder builder) {
		builder.append("<thead>");
		columns(builder);
		builder.append("</thead>");

		return builder;
	}

	public StringBuilder tBody() {
		return tHead(new StringBuilder());
	}

	public StringBuilder tBody(StringBuilder builder) {
		builder.append("<tbody>");
		rows(builder);
		builder.append("</tbody>");

		return builder;
	}

	public StringBuilder columns() {
		return columns(new StringBuilder());
	}

	public StringBuilder columns(StringBuilder builder) {
		int uidIndex = -1;
		Column c = table.column(keyField);

		if (c != null) {
			uidIndex = c.index();
		}

		builder.append("<tr>");
		for (int i = 0; i < table.columns().size(); i++) {
			if (i == uidIndex) {
				continue;
			}
			column(i, builder);
		}
		builder.append("</tr>");

		return builder;
	}

	public StringBuilder column(int index) {
		return column(index, new StringBuilder());
	}

	public StringBuilder column(int index, StringBuilder builder) {
		if (builder == null) {
			throw new IllegalArgumentException();
		}

		if (table.columns().size() < index) {
			return builder;
		}

		builder.append("<th name=\"" + table.columns().get(index).name() +"\">");
		builder.append(table.columns().get(index).name());
		builder.append("</th>");

		return builder;
	}

	public StringBuilder rows() {
		return rows(new StringBuilder());
	}

	public StringBuilder rows(StringBuilder builder) {
		int uidIndex = -1;
		Column c = table.column(keyField);
		String rowClass = "not-clickable-row";

		if (c != null) {
			uidIndex = c.index();
			rowClass = "clickable-row";
		}

		for (int i = 0; i < table.rows().size(); i++) {
			builder.append("<tr  class=\"" + rowClass + "\"");
			builder.append(uidIndex != -1 ? "id=\"row:"+ table.row(i).cells().get(uidIndex).value() + "\">" : "\">");
			row(i, builder);
			builder.append("</tr>");
		}

		return builder;
	}

	public StringBuilder row(int index) {
		return row(index, new StringBuilder());
	}

	public StringBuilder row(int index, StringBuilder builder) {
		if (builder == null) {
			throw new IllegalArgumentException();
		}

		if (table.rows().size() < index) {
			return builder;
		}

		Row row = table.row(index);
		List<Cell> rowCells = row.cells();

		int uidIndex = -1;
		Column c = table.column(keyField);

		if (c != null) {
			uidIndex = c.index();
		}

//		builder.append("<tr>");
		for (int i = 0; i < rowCells.size(); i++) {
			if (uidIndex == i) {
				continue;
			}

			builder.append("<th name=\"" + rowCells.get(i).key() +"\">");
			builder.append(rowCells.get(i).value());
			builder.append("</th>");
		}
//		builder.append("</tr>");

		return builder;
	}

	public String HTMLString() {
		return toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		tHead(builder);
		tBody(builder);
		return builder.toString();
	}
}
