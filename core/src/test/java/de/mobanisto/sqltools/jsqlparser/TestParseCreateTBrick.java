package de.mobanisto.sqltools.jsqlparser;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class TestParseCreateTBrick
{

	@Test
	public void test() throws JSQLParserException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/create-tbrick.sql");
		Statement statement = CCJSqlParserUtil.parse(input);
		CreateTable create = (CreateTable) statement;

		Assert.assertEquals("`tbrick`", create.getTable().getName());

		List<ColumnDefinition> columnDefinitions = create
				.getColumnDefinitions();
		Assert.assertEquals(4, columnDefinitions.size());

		Assert.assertEquals("`ID`", columnDefinitions.get(0).getColumnName());
		Assert.assertEquals("`identifier`",
				columnDefinitions.get(1).getColumnName());
		Assert.assertEquals("`beschreibung`",
				columnDefinitions.get(2).getColumnName());
		Assert.assertEquals("`flag_linkuse`",
				columnDefinitions.get(3).getColumnName());

		Assert.assertEquals("int",
				columnDefinitions.get(0).getColDataType().getDataType());
		Assert.assertEquals("varchar",
				columnDefinitions.get(1).getColDataType().getDataType());
		Assert.assertEquals("longtext",
				columnDefinitions.get(2).getColDataType().getDataType());
		Assert.assertEquals("tinyint",
				columnDefinitions.get(3).getColDataType().getDataType());
	}

}
