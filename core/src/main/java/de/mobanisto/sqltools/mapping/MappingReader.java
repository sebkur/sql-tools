package de.mobanisto.sqltools.mapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;

public class MappingReader
{

	public static Mapping read(InputStream input) throws IOException
	{
		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(reader);
		return read(br);
	}

	public static Mapping read(BufferedReader br) throws IOException
	{
		Mapping mapping = new Mapping();

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

	private static Splitter splitterDef = Splitter.on(",").limit(3)
			.trimResults();
	private static Pattern patternVariables = Pattern
			.compile("variables\\((.*)\\)");

	private static Splitter splitterComma = Splitter.on(",").trimResults();
	private static Splitter splitterArrow = Splitter.on("→").trimResults();

	private static void include(Mapping mapping, String def)
	{
		TableMapping tableMapping = parseInclude(def);
		if (tableMapping == null) {
			return;
		}
		mapping.includes.put(tableMapping.getTableName(), tableMapping);
	}

	private static void map(Mapping mapping, String def)
	{
		TableMapping tableMapping = parseInclude(def);
		if (tableMapping == null) {
			return;
		}
		mapping.mapped.put(tableMapping.getTableName(), tableMapping);
	}

	private static TableMapping parseInclude(String def)
	{
		List<String> parts = splitterDef.splitToList(def);
		if (parts.size() != 2 && parts.size() != 3) {
			System.out.println(String.format("Invalid include: '%s'", def));
			return null;
		}
		String tableName = parts.get(0);
		String className = parts.get(1);
		TableMapping mapping = new TableMapping(tableName, className);
		if (parts.size() == 3) {
			String extra = parts.get(2);
			Matcher matcher = patternVariables.matcher(extra);
			if (matcher.matches()) {
				parseVariables(mapping, matcher.group(1));
			}
		}
		return mapping;
	}

	private static void parseVariables(TableMapping mapping, String def)
	{
		for (String part : splitterComma.split(def)) {
			List<String> parts = splitterArrow.splitToList(part);
			String from = parts.get(0);
			String to = parts.get(1);
			mapping.mapColumnToVariable(from, to);
		}
	}

	private static void exclude(Mapping mapping, String def)
	{
		mapping.excludes.add(def.trim());
	}

}
