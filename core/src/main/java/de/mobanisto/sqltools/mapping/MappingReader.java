package de.mobanisto.sqltools.mapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.base.Splitter;

public class MappingReader
{

	public static Mapping read(InputStream input) throws IOException
	{
		Mapping mapping = new Mapping();

		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(reader);
		String line;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("include ")) {
				include(mapping, line.substring(8));
			} else if (line.startsWith("map ")) {
				map(mapping, line.substring(4));
			} else if (line.startsWith("exclude ")) {
				exclude(mapping, line.substring(8));
			}
		}

		return mapping;
	}

	private static Splitter splitter = Splitter.on(",").limit(2).trimResults();

	private static void include(Mapping mapping, String def)
	{
		List<String> parts = splitter.splitToList(def);
		if (parts.size() != 2) {
			System.out.println(String.format("Invalid include: '%s'", def));
			return;
		}
		String tableName = parts.get(0);
		String className = parts.get(1);
		mapping.includes.put(tableName, className);
	}

	private static void map(Mapping mapping, String def)
	{
		List<String> parts = splitter.splitToList(def);
		if (parts.size() != 2) {
			System.out.println(String.format("Invalid map: '%s'", def));
			return;
		}
		String tableName = parts.get(0);
		String className = parts.get(1);
		mapping.mapped.put(tableName, className);
	}

	private static void exclude(Mapping mapping, String def)
	{
		mapping.excludes.add(def.trim());
	}

}
