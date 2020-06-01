package de.mobanisto.sqltools;

import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpModelGenerator
{

	public ClassResult generate(CreateTable create)
	{
		return new PhpModelClassGenerator(create).generate();
	}

}
