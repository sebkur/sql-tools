package de.mobanisto.sqltools;

public class BaseGenerator
{

	private static final String nl = System.getProperty("line.separator");

	protected StringBuilder buffer;

	protected void a(String string)
	{
		buffer.append(string);
	}

	protected void l(String string)
	{
		buffer.append(string);
		buffer.append(nl);
	}

	protected void af(String string, Object... args)
	{
		buffer.append(String.format(string, args));
	}

	protected void lf(String string, Object... args)
	{
		buffer.append(String.format(string, args));
		buffer.append(nl);
	}

	protected void nl()
	{
		buffer.append(nl);
	}

}