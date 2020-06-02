package de.mobanisto.sqltools;

import java.io.BufferedReader;
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
import de.mobanisto.sqltools.mapping.Mapping;
import de.mobanisto.sqltools.mapping.MappingReader;
import de.mobanisto.sqltools.mapping.TableMapping;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class GeneratePhpModel
{

	public static void main(String[] args) throws IOException
	{
		if (args.length < 2 || args.length > 3) {
			System.out.println(
					"usage: generate-php-model <sql dump> [<mapping>] <output directory>");
			System.exit(1);
		}

		String argDumpfile = args[0];
		String argOutputDirectory = null;
		String argMapping = null;
		if (args.length == 2) {
			argOutputDirectory = args[1];
		} else if (args.length == 3) {
			argMapping = args[1];
			argOutputDirectory = args[2];
		}

		Path pathDumpfile = Paths.get(argDumpfile);
		Path dirOutput = Paths.get(argOutputDirectory);
		Path pathMapping = argMapping == null ? null : Paths.get(argMapping);

		GeneratePhpModel task = new GeneratePhpModel(pathDumpfile, pathMapping,
				dirOutput);
		task.execute();
	}

	private Path pathDumpfile;
	private Path pathMapping;
	private Path dirOutput;

	private Mapping mapping = null;

	public GeneratePhpModel(Path pathDumpfile, Path pathMapping, Path dirOutput)
	{
		this.pathDumpfile = pathDumpfile;
		this.pathMapping = pathMapping;
		this.dirOutput = dirOutput;
	}

	public void execute() throws IOException
	{
		if (pathMapping != null) {
			BufferedReader reader = Files.newBufferedReader(pathMapping);
			mapping = MappingReader.read(reader);
		}

		System.out.println("Creating output directory...");
		Files.createDirectories(dirOutput);

		System.out.println("Parsing MySQL dump...");
		InputStream inputSql = Files.newInputStream(pathDumpfile);

		ANTLRInputStream antlrInput = new ANTLRInputStream(inputSql);
		CaseChangingCharStream antlrInputUpper = new CaseChangingCharStream(
				antlrInput, true);
		MySqlLexer lexer = new MySqlLexer(antlrInputUpper);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		MySqlParser parser = new MySqlParser(tokenStream);
		parser.setErrorHandler(new BailErrorStrategy());

		RootContext root = parser.root();

		System.out.println("Finding CREATE TABLE statements...");
		CreateTableFinder finder = new CreateTableFinder();
		new ParseTreeWalker().walk(finder, root);

		System.out.println("Generating PHP classes...");
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

	private void createClass(String text)
			throws JSQLParserException, IOException
	{
		Statement statement = CCJSqlParserUtil.parse(text);
		CreateTable create = (CreateTable) statement;

		String tableName = MysqlUtil
				.unpackBackticks(create.getTable().getName());

		if (mapping == null) {
			ClassResult result = generator.generate(create);
			System.out.println(String.format("Including table: %s → %s",
					tableName, result.getClassName()));
			write(result);
			return;
		}

		if (mapping.getExcludes().contains(tableName)) {
			System.out.println("Ignoring excluded table: " + tableName);
			return;
		}

		if (mapping.getMapped().containsKey(tableName)) {
			TableMapping tableMapping = mapping.getMapped().get(tableName);
			System.out.println(String.format("Table mapped: %s → %s", tableName,
					tableMapping.getClassName()));
			return;
		}

		if (mapping.getIncludes().containsKey(tableName)) {
			TableMapping tableMapping = mapping.getIncludes().get(tableName);
			System.out.println(String.format("Including table: %s → %s",
					tableName, tableMapping.getClassName()));
			ClassResult result = generator.generate(create, tableMapping);
			write(result);
			return;
		}

		ClassResult result = generator.generate(create);
		System.out.println(String.format("Including unmapped table: %s → %s",
				tableName, result.getClassName()));
		write(result);
	}

	private void write(ClassResult result) throws IOException
	{
		Path file = dirOutput
				.resolve(String.format("%s.php", result.getClassName()));
		Files.write(file, result.getContent().getBytes());
	}

}
