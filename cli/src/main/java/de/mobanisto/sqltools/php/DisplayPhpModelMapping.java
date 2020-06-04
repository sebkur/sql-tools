package de.mobanisto.sqltools.php;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.mobanisto.antlr.AntlrUtil;
import de.mobanisto.antlr.CaseChangingCharStream;
import de.mobanisto.antlr.mysql.MySqlLexer;
import de.mobanisto.antlr.mysql.MySqlParser;
import de.mobanisto.antlr.mysql.MySqlParser.ColumnCreateTableContext;
import de.mobanisto.antlr.mysql.MySqlParser.RootContext;
import de.mobanisto.sqltools.mysql.CreateTableFinder;
import de.mobanisto.sqltools.mysql.MysqlUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class DisplayPhpModelMapping
{

	public static void main(String[] args) throws IOException
	{
		if (args.length != 1) {
			System.out.println("usage: display-php-model-mapping <sql dump>");
			System.exit(1);
		}

		String argDumpfile = args[0];

		Path pathDumpfile = Paths.get(argDumpfile);

		DisplayPhpModelMapping task = new DisplayPhpModelMapping(pathDumpfile);
		task.execute();
	}

	private Path pathDumpfile;

	public DisplayPhpModelMapping(Path pathDumpfile)
	{
		this.pathDumpfile = pathDumpfile;
	}

	public void execute() throws IOException
	{
		InputStream inputSql = Files.newInputStream(pathDumpfile);

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

		for (ColumnCreateTableContext create : finder.getList()) {
			String text = AntlrUtil.getText(antlrInput, create);
			try {
				display(text);
			} catch (JSQLParserException e) {
				System.out.println("Error encountered: " + e.getMessage());
			}
		}
	}

	private PhpModelGenerator generator = new PhpModelGenerator();

	private void display(String text) throws JSQLParserException
	{
		Statement statement = CCJSqlParserUtil.parse(text);
		CreateTable create = (CreateTable) statement;

		String tableName = create.getTable().getName();
		tableName = MysqlUtil.unpackBackticks(tableName);

		ClassResult result = generator.generate(create);
		String className = result.getClassName();

		System.out.println(String.format("%s, %s", tableName, className));
	}

}
