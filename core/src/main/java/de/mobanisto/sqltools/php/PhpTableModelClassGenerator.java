package de.mobanisto.sqltools.php;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mobanisto.sqltools.mapping.TableMapping;
import de.mobanisto.sqltools.mysql.MysqlUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpTableModelClassGenerator extends BasePhpGenerator
{

	private CreateTable create;
	private TableMapping tableMapping;

	public PhpTableModelClassGenerator(CreateTable create)
	{
		this.create = create;
	}

	public PhpTableModelClassGenerator(CreateTable create,
			TableMapping tableMapping)
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
		String tableClassName = tableMapping.getClassName() + "Table";

		List<String> columnNames = new ArrayList<>();
		Map<String, String> columnAliases = new HashMap<>();
		for (ColumnDefinition definition : create.getColumnDefinitions()) {
			String columnName = columnName(definition.getColumnName());
			columnNames.add(columnName);
			String alias = tableMapping.getColumnToVariable().get(columnName);
			if (alias != null) {
				columnAliases.put(columnName, alias);
			}
		}

		List<String> uses = new ArrayList<>();
		uses.add("tablemodel\\Table");
		phpClass(tableClassName, "Table", "tablemodel\\tables", uses, true);

		a("    public function __construct()");
		nl();
		l("    {");
		lf("        parent::__construct(\"%s\", \"%s\");",
				tableMapping.getTableName(), tableMapping.getClassName());
		for (String columnName : columnNames) {
			if (!columnAliases.containsKey(columnName)) {
				lf("        $this->addRow(\"%s\");", columnName);
			} else {
				String alias = columnAliases.get(columnName);
				lf("        $this->addAliasRow(\"%s\", \"%s\");", columnName,
						alias);
			}
		}
		l("    }");
		nl();

		a("}");

		return new ClassResult(tableClassName, buffer.toString());
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

}
