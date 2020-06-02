package de.mobanisto.sqltools;

import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpModelGenerator
{

	public ClassResult generate(CreateTable create)
	{
		return new PhpModelClassGenerator(create).generate();
	}

	public ClassResult generate(CreateTable create, String className)
	{
		return new PhpModelClassGenerator(create, className).generate();
	}

}
