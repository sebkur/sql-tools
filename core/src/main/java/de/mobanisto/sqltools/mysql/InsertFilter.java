package de.mobanisto.sqltools.mysql;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.mobanisto.antlr.CaseChangingCharStream;
import de.mobanisto.antlr.mysql.MySqlLexer;
import de.mobanisto.antlr.mysql.MySqlParser;
import de.mobanisto.antlr.mysql.MySqlParser.RootContext;
import de.mobanisto.antlr.mysql.MySqlParser.SqlStatementContext;
import de.mobanisto.antlr.mysql.MySqlParser.SqlStatementsContext;
import de.mobanisto.antlr.mysql.MySqlParserBaseListener;

public class InsertFilter
{

	private InputStream input;
	private OutputStream output;
	private DataOutputStream dataOutput;

	private MySqlLexer lexer;
	private CommonTokenStream tokenStream;
	private MySqlParser parser;

	public InsertFilter(InputStream input, OutputStream output)
	{
		this.input = input;
		this.output = output;

		dataOutput = new DataOutputStream(output);
	}

	public void execute() throws IOException
	{
		ANTLRInputStream antlrInput = new ANTLRInputStream(input);
		CaseChangingCharStream antlrInputUpper = new CaseChangingCharStream(
				antlrInput, true);
		lexer = new MySqlLexer(antlrInputUpper);
		tokenStream = new CommonTokenStream(lexer);
		parser = new MySqlParser(tokenStream);
		parser.setErrorHandler(new BailErrorStrategy());

		RootContext root = parser.root();

		StatementFinder finder = new StatementFinder();
		new ParseTreeWalker().walk(finder, root);
	}

	public class StatementFinder extends MySqlParserBaseListener
	{

		private SqlStatementContext last = null;
		private Token lastSemi = null;

		@Override
		public void enterSqlStatement(SqlStatementContext ctx)
		{
			try {
				handleStatement(ctx);
			} catch (IOException e) {
				// ignore
			}
		}

		@Override
		public void exitSqlStatements(SqlStatementsContext ctx)
		{
			try {
				handleRemainder(ctx);
			} catch (IOException e) {
				// ignore
			}
		}

		private void handleRemainder(SqlStatementsContext ctx)
				throws IOException
		{
			if (last == null) {
				return;
			}

			CharStream cs = ctx.getStart().getInputStream();
			int size = cs.size();
			Interval interval = new Interval(lastSemi.getStopIndex() + 1, size);

			String text = cs.getText(interval);
			dataOutput.writeBytes(text);
		}

		private void handleStatement(SqlStatementContext ctx) throws IOException
		{
			CharStream charStream = ctx.getStart().getInputStream();

			boolean skipStatement = ctx.getStart()
					.getType() == MySqlLexer.INSERT;

			// find the next SEMI token to include it with the current statement
			int offsetNextSemi = 1;
			boolean foundNextSemi = false;
			while (!foundNextSemi) {
				Token token = tokenStream
						.get(ctx.getStop().getTokenIndex() + offsetNextSemi);
				if (token.getType() == MySqlLexer.SEMI) {
					foundNextSemi = true;
					break;
				}
				offsetNextSemi += 1;
			}
			Token tokenNextSemi = tokenStream
					.get(ctx.getStop().getTokenIndex() + offsetNextSemi);

			int from = 0;
			if (last != null) {
				from = lastSemi.getStopIndex() + 1;
			}
			int to = ctx.getStart().getStartIndex() - 1;

			Interval intervalBefore = new Interval(from, to);
			String textBefore = charStream.getText(intervalBefore);
			dataOutput.writeBytes(textBefore);

			Interval intervalStatement = new Interval(ctx.start.getStartIndex(),
					tokenNextSemi.getStopIndex());
			String textStatement = charStream.getText(intervalStatement);

			if (!skipStatement) {
				dataOutput.writeBytes(textStatement);
			}

			last = ctx;
			lastSemi = tokenNextSemi;
		}

	}

}
