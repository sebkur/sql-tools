package de.mobanisto.sqltools.mediawiki;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import de.topobyte.luqe.iface.QueryException;
import net.sf.jsqlparser.JSQLParserException;

public class ExtractIdAndTitle
{

	public static void main(String[] args) throws IOException,
			JSQLParserException, QueryException, SQLException
	{
		if (args.length != 2) {
			System.out.println(
					"usage: extract-id-and-title <input pages dump> <output sqlite database>");
			System.exit(1);
		}
		Path input = Paths.get(args[0]);
		Path output = Paths.get(args[1]);
		IdAndTitleExtractor task = new IdAndTitleExtractor(input, output);
		task.execute();
	}

}
