package de.mobanisto.sqltools;

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
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class GeneratePhpModel
{

	public static void main(String[] args) throws IOException
	{
		if (args.length != 2) {
			System.out.println(
					"usage: generate-php-model <sql dump> <output directory>");
			System.exit(1);
		}

		String argDumpfile = args[0];
		String argOutputDirectory = args[1];

		Path pathDumpfile = Paths.get(argDumpfile);
		Path dirOutput = Paths.get(argOutputDirectory);

		GeneratePhpModel task = new GeneratePhpModel(pathDumpfile, dirOutput);
		task.execute();
	}

	private Path pathDumpfile;
	private Path dirOutput;

	public GeneratePhpModel(Path pathDumpfile, Path dirOutput)
	{
		this.pathDumpfile = pathDumpfile;
		this.dirOutput = dirOutput;
	}

	public void execute() throws IOException
	{
		Files.createDirectories(dirOutput);

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
				createClass(text);
			} catch (JSQLParserException e) {
				System.out.println("Error encountered: " + e.getMessage());
			}
		}
	}

	private PhpModelGenerator generator = new PhpModelGenerator();

	private void createClass(String text) throws JSQLParserException
	{
		Statement statement = CCJSqlParserUtil.parse(text);
		CreateTable create = (CreateTable) statement;

		String tableName = create.getTable().getName();
		System.out.println(tableName);

		String code = generator.generate(create);
	}

}
