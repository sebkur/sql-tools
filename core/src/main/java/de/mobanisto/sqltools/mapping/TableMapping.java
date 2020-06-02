package de.mobanisto.sqltools.mapping;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class TableMapping
{

	@Getter
	private String tableName;
	@Getter
	private String className;
	@Getter
	private Map<String, String> columnToVariable = new HashMap<>();

	public TableMapping(String tableName, String className)
	{
		this.tableName = tableName;
		this.className = className;
	}

	void mapColumnToVariable(String column, String variable)
	{
		columnToVariable.put(column, variable);
	}

}
