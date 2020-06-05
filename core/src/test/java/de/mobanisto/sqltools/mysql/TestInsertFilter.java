package de.mobanisto.sqltools.mysql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import net.sf.jsqlparser.JSQLParserException;

public class TestInsertFilter
{

	@Test
	public void testSingleInserts() throws IOException, JSQLParserException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/dump.sql");
		InputStream inputExpected = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("mysql/dump-without-inserts.sql");
		String expected = IOUtils.toString(inputExpected,
				StandardCharsets.UTF_8);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InsertFilter statementFilter = new InsertFilter(input, output);

		statementFilter.execute();

		String result = new String(output.toByteArray());
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testMultipleInserts() throws IOException, JSQLParserException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/dump2.sql");
		InputStream inputExpected = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("mysql/dump-without-inserts.sql");
		String expected = IOUtils.toString(inputExpected,
				StandardCharsets.UTF_8);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InsertFilter statementFilter = new InsertFilter(input, output);

		statementFilter.execute();

		String result = new String(output.toByteArray());
		Assert.assertEquals(expected, result);
	}

}
