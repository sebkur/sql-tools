package de.mobanisto.sqltools.mediawiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.IPreparedStatement;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.SqliteDatabase;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

public class IdAndTitleExtractor
{

	private Path input;
	private Path output;

	private int maxLines = Integer.MAX_VALUE;

	public IdAndTitleExtractor(Path input, Path output)
	{
		this.input = input;
		this.output = output;
	}

	public void setMaxLines(int maxLines)
	{
		this.maxLines = maxLines;
	}

	public void execute() throws IOException, QueryException, SQLException
	{
		setupDatabase();

		parseInput();

		database.closeConnection(true);
	}

	private SqliteDatabase database;
	private IConnection connection;
	private IPreparedStatement stmtInsert = null;

	private void setupDatabase() throws QueryException
	{
		database = new SqliteDatabase(output);

		connection = database.getConnection();
		connection.execute("CREATE TABLE pages (id integer, title varchar)");
	}

	private void insert(long id, String title) throws QueryException
	{
		if (stmtInsert == null) {
			stmtInsert = connection
					.prepareStatement("INSERT INTO pages VALUES(?, ?)");
		}
		stmtInsert.setLong(1, id);
		stmtInsert.setString(2, title);
		stmtInsert.execute();
	}

	private void parseInput() throws IOException, QueryException, SQLException
	{
		GZIPInputStream is = new GZIPInputStream(Files.newInputStream(input));
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		int lineCount = 0;
		int count = 0;

		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			if (!line.startsWith("INSERT INTO")) {
				continue;
			}

			line = replaceSingleQuoteEscape(line);

			try {
				Statement statement = CCJSqlParserUtil.parse(line);
				count += parse(statement);
			} catch (JSQLParserException e) {
				Path file = Files.createTempFile("sqltools", ".sql");
				e.printStackTrace();
				System.out.println(file);

				Files.write(file, line.getBytes());
			}
			System.out
					.println(String.format("line %d: %d", ++lineCount, count));

			database.getJdbcConnection().commit();

			if (lineCount >= maxLines) {
				break;
			}
		}
	}

	private int parse(Statement statement) throws QueryException
	{
		int count = 0;
		Insert insert = (Insert) statement;
		ItemsList items = insert.getItemsList();
		MultiExpressionList mel = (MultiExpressionList) items;
		List<ExpressionList> listOfExpressionLists = mel.getExprList();
		for (ExpressionList list : listOfExpressionLists) {
			List<Expression> expressions = list.getExpressions();
			long id = ((LongValue) expressions.get(0)).getValue();
			long namespace = ((LongValue) expressions.get(1)).getValue();
			String title = ((StringValue) expressions.get(2)).getValue();
			if (namespace != 0) {
				continue;
			}
			insert(id, title);
			count++;
		}
		return count;
	}

	/**
	 * We need to replace \' with '' because JSQLParser does not support the
	 * non-standard escaping that MySQL is using. However we need to take care
	 * not to mess up quoted strings that end with a backslash such as 'foo\'. I
	 * could'nt come up with a regular expression to do this, so I wrote this
	 * method.
	 */
	private String replaceSingleQuoteEscape(String line)
	{
		boolean lastSlash = false;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (lastSlash) {
				if (c == '\\') {
					buffer.append("\\\\");
				} else if (c == '\'') {
					buffer.append("''");
				} else {
					buffer.append("\\");
					buffer.append(c);
				}
				lastSlash = false;
				continue;
			}
			if (c == '\\') {
				lastSlash = true;
				continue;
			}
			buffer.append(c);
		}
		return buffer.toString();
	}

}
