package de.mobanisto.sqltools.antlr;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;

import de.mobanisto.antlr.CaseChangingCharStream;
import de.mobanisto.antlr.mysql.MySqlLexer;
import de.mobanisto.antlr.mysql.MySqlParser;
import de.mobanisto.antlr.mysql.MySqlParser.ColumnCreateTableContext;
import de.mobanisto.antlr.mysql.MySqlParser.RootContext;
import de.mobanisto.sqltools.mysql.CreateTableFinder;

public class TestParseCreateTBrick
{

	/**
	 * Make sure that we can parse an SQL snippet that is already in
	 * all-uppercase format.
	 */
	@Test
	public void testUppercase() throws IOException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/create-tbrick-uppercase.sql");

		ANTLRInputStream antlrInput = new ANTLRInputStream(input);
		MySqlLexer lexer = new MySqlLexer(antlrInput);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		MySqlParser parser = new MySqlParser(tokenStream);
		parser.setErrorHandler(new BailErrorStrategy());

		RootContext root = parser.root();

		CreateTableFinder finder = new CreateTableFinder();
		new ParseTreeWalker().walk(finder, root);
	}

	/**
	 * If we try to parse a file with the normal formatting, that fails, because
	 * the grammar expects all keywords to be in uppercase format.
	 */
	@Test(expected = ParseCancellationException.class)
	public void testNoUppercase() throws IOException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/create-tbrick.sql");

		ANTLRInputStream antlrInput = new ANTLRInputStream(input);
		MySqlLexer lexer = new MySqlLexer(antlrInput);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		MySqlParser parser = new MySqlParser(tokenStream);
		parser.setErrorHandler(new BailErrorStrategy());

		parser.root();
	}

	/**
	 * Make sure that parsing the normally formatted file works using the
	 * CaseChangingCharStream.
	 */
	@Test
	public void testNoUppercaseCaseChanging() throws IOException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/create-tbrick.sql");

		ANTLRInputStream antlrInput = new ANTLRInputStream(input);
		CaseChangingCharStream antlrInputUpper = new CaseChangingCharStream(
				antlrInput, true);
		MySqlLexer lexer = new MySqlLexer(antlrInputUpper);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		MySqlParser parser = new MySqlParser(tokenStream);
		parser.setErrorHandler(new BailErrorStrategy());

		RootContext root = parser.root();

		CreateTableFinder finder = new CreateTableFinder();
		new ParseTreeWalker().walk(finder, root);
	}

	/**
	 * Make sure we can spot the correct part of the original SQL text.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSourcePositions() throws IOException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/create-tbrick.sql");

		ANTLRInputStream antlrInput = new ANTLRInputStream(input);
		CaseChangingCharStream antlrInputUpper = new CaseChangingCharStream(
				antlrInput, true);
		MySqlLexer lexer = new MySqlLexer(antlrInputUpper);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		MySqlParser parser = new MySqlParser(tokenStream);
		parser.setErrorHandler(new BailErrorStrategy());

		RootContext root = parser.root();

		CreateTableFinder finder = new CreateTableFinder();
		new ParseTreeWalker().walk(finder, root);
		List<ColumnCreateTableContext> list = finder.getList();
		Assert.assertEquals(1, list.size());

		ColumnCreateTableContext create = list.get(0);
		Token start = create.getStart();
		Token stop = create.getStop();
		Assert.assertEquals(1, start.getLine());
		Assert.assertEquals(9, stop.getLine());
		Assert.assertEquals(0, start.getCharPositionInLine());
		Assert.assertEquals(58,
				stop.getCharPositionInLine() + stop.getText().length());
	}

}
