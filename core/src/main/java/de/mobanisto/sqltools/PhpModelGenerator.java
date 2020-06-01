package de.mobanisto.sqltools;

import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpModelGenerator
{

	public String generate(CreateTable create)
	{
		return new PhpModelClassGenerator(create).generate();
	}

}
