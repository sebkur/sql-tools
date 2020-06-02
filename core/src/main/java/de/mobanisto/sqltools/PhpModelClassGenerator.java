package de.mobanisto.sqltools;

import java.util.ArrayList;
import java.util.List;

import de.mobanisto.sqltools.mapping.TableMapping;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpModelClassGenerator extends BasePhpGenerator
{

	private CreateTable create;
	private TableMapping tableMapping;

	public PhpModelClassGenerator(CreateTable create)
	{
		this.create = create;
	}

	public PhpModelClassGenerator(CreateTable create, TableMapping tableMapping)
	{
		this.create = create;
		this.tableMapping = tableMapping;
	}

	public ClassResult generate()
	{
		buffer = new StringBuilder();

		if (tableMapping == null) {
			String tableName = create.getTable().getName();
			tableMapping = new TableMapping(
					MysqlUtil.unpackBackticks(tableName), className(tableName));
		}

		List<String> columnNames = new ArrayList<>();
		List<String> variableNames = new ArrayList<>();
		for (ColumnDefinition definition : create.getColumnDefinitions()) {
			String columnName = columnName(definition.getColumnName());
			columnNames.add(columnName);
			String variable = tableMapping.getColumnToVariable()
					.get(columnName);
			if (variable == null) {
				variable = variableName(columnName);
			}
			variableNames.add(variable);
		}

		phpClass(tableMapping.getClassName(), true);

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
		af("        return new %s(", tableMapping.getClassName());
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

		return new ClassResult(tableMapping.getClassName(), buffer.toString());
	}

	private String className(String name)
	{
		String base = MysqlUtil.unpackBackticks(name);
		if (base.startsWith("t")) {
			base = base.substring(1);
		}
		String first = base.substring(0, 1);
		String remainder = base.substring(1);
		return first.toUpperCase() + remainder;
	}

	private String columnName(String columnName)
	{
		return MysqlUtil.unpackBackticks(columnName);
	}

	private String variableName(String columnName)
	{
		String base = MysqlUtil.unpackBackticks(columnName);
		return base.toLowerCase();
	}

}
