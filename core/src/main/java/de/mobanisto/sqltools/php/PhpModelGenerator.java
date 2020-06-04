package de.mobanisto.sqltools.php;

import de.mobanisto.sqltools.mapping.TableMapping;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpModelGenerator
{

	public ClassResult generate(CreateTable create)
	{
		return new PhpModelClassGenerator(create).generate();
	}

	public ClassResult generate(CreateTable create, TableMapping tableMapping)
	{
		return new PhpModelClassGenerator(create, tableMapping).generate();
	}

}
