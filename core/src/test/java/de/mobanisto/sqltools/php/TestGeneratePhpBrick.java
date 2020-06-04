package de.mobanisto.sqltools.php;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import de.mobanisto.antlr.AntlrUtil;
import de.mobanisto.antlr.CaseChangingCharStream;
import de.mobanisto.antlr.mysql.MySqlLexer;
import de.mobanisto.antlr.mysql.MySqlParser;
import de.mobanisto.antlr.mysql.MySqlParser.ColumnCreateTableContext;
import de.mobanisto.antlr.mysql.MySqlParser.RootContext;
import de.mobanisto.sqltools.mysql.CreateTableFinder;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class TestGeneratePhpBrick
{

	/**
	 * Make sure we can spot the correct part of the original SQL text.
	 */
	@Test
	public void testSourcePositions() throws IOException, JSQLParserException
	{
		InputStream inputSql = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/create-tbrick.sql");

		ANTLRInputStream antlrInput = new ANTLRInputStream(inputSql);
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

		ColumnCreateTableContext ctx = list.get(0);
		String text = AntlrUtil.getText(antlrInput, ctx);

		Statement statement = CCJSqlParserUtil.parse(text);
		CreateTable create = (CreateTable) statement;

		PhpModelGenerator generator = new PhpModelGenerator();
		ClassResult result = generator.generate(create);

		InputStream inputPhp = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/Brick.php");
		String expectedCode = IOUtils.toString(inputPhp,
				StandardCharsets.UTF_8);
		Assert.assertEquals(expectedCode, result.getContent());
	}

}
