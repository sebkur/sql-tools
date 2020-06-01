package de.mobanisto.sqltools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpModelGenerator
{

	private static final String nl = System.getProperty("line.separator");

	public String generate(CreateTable create)
	{
		StringBuilder buffer = new StringBuilder();

		String tableName = className(create.getTable().getName());

		buffer.append("<?php" + nl + nl);
		buffer.append("class " + tableName + nl);
		buffer.append("{" + nl);
		buffer.append(nl);

		List<String> variableNames = new ArrayList<>();
		for (ColumnDefinition definition : create.getColumnDefinitions()) {
			variableNames.add(variableName(definition.getColumnName()));
		}

		for (String variable : variableNames) {
			buffer.append("    public $");
			buffer.append(variable);
			buffer.append(";");
			buffer.append(nl);
		}

		buffer.append(nl);
		buffer.append("    public function __construct(");
		for (int i = 0; i < variableNames.size(); i++) {
			buffer.append("$");
			buffer.append(variableNames.get(i));
			if (i != variableNames.size() - 1) {
				buffer.append(", ");
			}
		}
		buffer.append(")");
		buffer.append(nl);
		buffer.append("    {" + nl);
		for (String variable : variableNames) {
			buffer.append(String.format("        $this->%s = %s;", variable,
					variable));
			buffer.append(nl);
		}
		buffer.append("    }" + nl);
		buffer.append(nl);

		buffer.append("}");

		return buffer.toString();
	}

	private static final Pattern patternBacktick = Pattern.compile("`(.*)`");

	private String className(String name)
	{
		String base = name;
		Matcher matcher = patternBacktick.matcher(name);
		if (matcher.matches()) {
			base = matcher.group(1);
		}
		if (base.startsWith("t")) {
			base = base.substring(1);
		}
		String first = base.substring(0, 1);
		String remainder = base.substring(1);
		return first.toUpperCase() + remainder;
	}

	private String variableName(String columnName)
	{
		String base = columnName;
		Matcher matcher = patternBacktick.matcher(columnName);
		if (matcher.matches()) {
			base = matcher.group(1);
		}
		return base.toLowerCase();
	}

}
