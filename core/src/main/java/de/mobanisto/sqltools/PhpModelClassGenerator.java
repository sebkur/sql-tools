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

	private void l(String string)
	{
		buffer.append(string);
		buffer.append(nl);
	}

	private void af(String string, Object... args)
	{
		buffer.append(String.format(string, args));
	}

	private void lf(String string, Object... args)
	{
		buffer.append(String.format(string, args));
		buffer.append(nl);
	}

	private void nl()
	{
		buffer.append(nl);
	}

	public String generate()
	{
		buffer = new StringBuilder();

		String className = className(create.getTable().getName());

		List<String> columnNames = new ArrayList<>();
		List<String> variableNames = new ArrayList<>();
		for (ColumnDefinition definition : create.getColumnDefinitions()) {
			columnNames.add(columnName(definition.getColumnName()));
			variableNames.add(variableName(definition.getColumnName()));
		}

		l("<?php");
		nl();
		l("class " + className);
		l("{");
		nl();

		for (String variable : variableNames) {
			lf("    public $%s;", variable);
		}

		nl();
		a("    public function __construct(");
		for (int i = 0; i < variableNames.size(); i++) {
			a("$");
			a(variableNames.get(i));
			if (i != variableNames.size() - 1) {
				a(", ");
			}
		}
		a(")");
		nl();
		l("    {");
		for (String variable : variableNames) {
			lf("        $this->%s = $%s;", variable, variable);
		}
		l("    }");
		nl();

		l("    /** @var DB_PD $handle */");
		l("    public static function from_handle($handle)");
		l("    {");
		for (int i = 0; i < variableNames.size(); i++) {
			String variable = variableNames.get(i);
			String columnName = columnNames.get(i);
			lf("        $%s = $handle->f(\"%s\");", variable, columnName);
		}
		af("        return new %s(", className);
		for (int i = 0; i < variableNames.size(); i++) {
			a("$");
			a(variableNames.get(i));
			if (i != variableNames.size() - 1) {
				a(", ");
			}
		}
		l(");");
		l("    }");
		nl();

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

	private String columnName(String columnName)
	{
		return unpackBackticks(columnName);
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
