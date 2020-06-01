package de.mobanisto.sqltools;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpModelGenerator
{

	private static final String nl = System.getProperty("line.separator");

	public void generate(CreateTable create)
	{
		StringBuilder buffer = new StringBuilder();

		String tableName = create.getTable().getName();

		buffer.append("<?php" + nl + nl);
		buffer.append("class " + tableName + nl);
		buffer.append("{" + nl);
		buffer.append(nl);

		List<String> names = new ArrayList<>();
		for (ColumnDefinition definition : create.getColumnDefinitions()) {
			names.add(definition.getColumnName());
		}

		for (String colName : names) {
			buffer.append("    public $");
			buffer.append(colName);
			buffer.append(";");
			buffer.append(nl);
		}

		buffer.append(nl);
		buffer.append("    public function __construct(");
		for (int i = 0; i < names.size(); i++) {
			buffer.append(names.get(i));
			if (i != names.size() - 1) {
				buffer.append(", ");
			}
		}
		buffer.append(")");
		buffer.append(nl);
		buffer.append("    {" + nl);
		buffer.append("    }" + nl);
		buffer.append(nl);

		buffer.append("}");

		System.out.println(buffer.toString());
	}

}
