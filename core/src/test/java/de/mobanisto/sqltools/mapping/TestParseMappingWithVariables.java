package de.mobanisto.sqltools.mapping;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

public class TestParseMappingWithVariables
{

	@Test
	public void test() throws IOException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("mysql/mapping-with-variables");
		Mapping mapping = MappingReader.read(input);

		Assert.assertEquals(2, mapping.getIncludes().size());
		Assert.assertEquals(0, mapping.getMapped().size());
		Assert.assertEquals(0, mapping.getExcludes().size());

		Assert.assertEquals("Blog",
				mapping.getIncludes().get("tblog").getClassName());
		Assert.assertEquals("Brick",
				mapping.getIncludes().get("tbrick").getClassName());

		TableMapping mappingBlog = mapping.getIncludes().get("tblog");
		Assert.assertEquals(2, mappingBlog.getColumnToVariable().size());

		TableMapping mappingBrick = mapping.getIncludes().get("tbrick");
		Assert.assertEquals(1, mappingBrick.getColumnToVariable().size());

		Assert.assertEquals("sprache",
				mappingBlog.getColumnToVariable().get("id_sprache"));
		Assert.assertEquals("author",
				mappingBlog.getColumnToVariable().get("id_author"));

		Assert.assertEquals("linkuse",
				mappingBrick.getColumnToVariable().get("flag_linkuse"));
	}

}
