package de.mobanisto.sqltools;

import de.mobanisto.antlr.mysql.MySqlParser.ColumnCreateTableContext;
import de.mobanisto.antlr.mysql.MySqlParserBaseListener;

public class CreateTableFinder extends MySqlParserBaseListener
{

	@Override
	public void enterColumnCreateTable(ColumnCreateTableContext ctx)
	{
		System.out.println(ctx.getText());
	}

}
