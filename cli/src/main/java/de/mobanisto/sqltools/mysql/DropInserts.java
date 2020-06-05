package de.mobanisto.sqltools.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DropInserts
{

	public static void main(String[] args) throws IOException
	{
		if (args.length != 2) {
			System.out.println("usage: drop-inserts <sql dump> <output>");
			System.exit(1);
		}

		String argDumpfile = args[0];
		String argOutput = args[1];

		Path pathDumpfile = Paths.get(argDumpfile);
		Path pathOutput = Paths.get(argOutput);

		DropInserts task = new DropInserts(pathDumpfile, pathOutput);
		task.execute();
	}

	private Path pathDumpfile;
	private Path pathOutput;

	public DropInserts(Path pathDumpfile, Path pathOutput)
	{
		this.pathDumpfile = pathDumpfile;
		this.pathOutput = pathOutput;
	}

	public void execute() throws IOException
	{
		InputStream input = Files.newInputStream(pathDumpfile);
		OutputStream output = Files.newOutputStream(pathOutput);
		InsertFilter filter = new InsertFilter(input, output);

		filter.execute();
	}

}
