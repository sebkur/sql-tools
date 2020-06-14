package de.mobanisto.sqltools.php;

import de.mobanisto.sqltools.mapping.TableMapping;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class PhpTableModelGenerator
{

	public ClassResult generate(CreateTable create)
	{
		return new PhpTableModelClassGenerator(create).generate();
	}

	public ClassResult generate(CreateTable create, TableMapping tableMapping)
	{
		return new PhpTableModelClassGenerator(create, tableMapping).generate();
	}

}
