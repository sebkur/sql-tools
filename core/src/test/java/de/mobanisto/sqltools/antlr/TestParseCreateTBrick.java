package de.mobanisto.sqltools.antlr;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import de.mobanisto.antlr.CaseChangingCharStream;
import de.mobanisto.antlr.mysql.MySqlLexer;
import de.mobanisto.antlr.mysql.MySqlParser;
import de.mobanisto.antlr.mysql.MySqlParser.RootContext;
import de.mobanisto.sqltools.CreateTableFinder;

public class TestParseCreateTBrick
{

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

	@Test
	public void testNoUppercaseCaseChanging() throws IOException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/create-tbrick-uppercase.sql");

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

}
