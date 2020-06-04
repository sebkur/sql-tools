package de.mobanisto.sqltools.mysql;

import java.util.ArrayList;
import java.util.List;

import de.mobanisto.antlr.mysql.MySqlParser.ColumnCreateTableContext;
import de.mobanisto.antlr.mysql.MySqlParserBaseListener;

public class CreateTableFinder extends MySqlParserBaseListener
{

	private List<ColumnCreateTableContext> list = new ArrayList<>();

	@Override
	public void enterColumnCreateTable(ColumnCreateTableContext ctx)
	{
		list.add(ctx);
	}

	public List<ColumnCreateTableContext> getList()
	{
		return list;
	}

}
