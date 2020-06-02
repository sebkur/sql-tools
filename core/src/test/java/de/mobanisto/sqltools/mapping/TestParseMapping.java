package de.mobanisto.sqltools.mapping;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

public class TestParseMapping
{

	@Test
	public void test() throws IOException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/mapping");
		Mapping mapping = MappingReader.read(input);

		Assert.assertEquals(3, mapping.getIncludes().size());
		Assert.assertEquals(1, mapping.getMapped().size());
		Assert.assertEquals(2, mapping.getExcludes().size());

		Assert.assertEquals("Blog",
				mapping.getIncludes().get("tblog").getClassName());
		Assert.assertEquals("Brick",
				mapping.getIncludes().get("tbrick").getClassName());
		Assert.assertEquals("BrickText",
				mapping.getIncludes().get("tbrick_text").getClassName());

		Assert.assertEquals("BrickText",
				mapping.getMapped().get("tbrick_text_archiv").getClassName());

		Assert.assertTrue(mapping.getExcludes().contains("tblog2personen"));
		Assert.assertTrue(mapping.getExcludes().contains("tblog2templates"));
	}

}
