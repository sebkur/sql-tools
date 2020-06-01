package de.mobanisto.sqltools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpModelClassGenerator
{

	private static final String nl = System.getProperty("line.separator");

	private CreateTable create;

	public PhpModelClassGenerator(CreateTable create)
	{
		this.create = create;
	}

	private StringBuilder buffer;

	private void a(String string)
	{
		buffer.append(string);
	}

	public String generate()
	{
		buffer = new StringBuilder();

		String tableName = className(create.getTable().getName());

		a("<?php" + nl + nl);
		a("class " + tableName + nl);
		a("{" + nl);
		a(nl);

		List<String> variableNames = new ArrayList<>();
		for (ColumnDefinition definition : create.getColumnDefinitions()) {
			variableNames.add(variableName(definition.getColumnName()));
		}

		for (String variable : variableNames) {
			a("    public $");
			a(variable);
			a(";");
			a(nl);
		}

		a(nl);
		a("    public function __construct(");
		for (int i = 0; i < variableNames.size(); i++) {
			a("$");
			a(variableNames.get(i));
			if (i != variableNames.size() - 1) {
				a(", ");
			}
		}
		a(")");
		a(nl);
		a("    {" + nl);
		for (String variable : variableNames) {
			a(String.format("        $this->%s = $%s;", variable, variable));
			a(nl);
		}
		a("    }" + nl);
		a(nl);

		a("}");

		return buffer.toString();
	}

	private static final Pattern patternBacktick = Pattern.compile("`(.*)`");

	private String className(String name)
	{
		String base = unpackBackticks(name);
		if (base.startsWith("t")) {
			base = base.substring(1);
		}
		String first = base.substring(0, 1);
		String remainder = base.substring(1);
		return first.toUpperCase() + remainder;
	}

	private String variableName(String columnName)
	{
		String base = unpackBackticks(columnName);
		return base.toLowerCase();
	}

	private String unpackBackticks(String name)
	{
		Matcher matcher = patternBacktick.matcher(name);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return name;
	}

}
