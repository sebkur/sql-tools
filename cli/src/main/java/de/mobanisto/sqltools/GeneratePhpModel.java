package de.mobanisto.sqltools;

public class GeneratePhpModel
{

	public static void main(String[] args)
	{
		if (args.length != 2) {
			System.out.println(
					"usage: generate-php-model <sql dump> <output directory>");
			System.exit(1);
		}

		String argDumpfile = args[0];
		String argOutputDirectory = args[1];

		new PhpModelGenerator();
	}

}
