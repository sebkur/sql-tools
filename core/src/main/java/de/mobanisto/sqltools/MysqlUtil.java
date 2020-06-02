package de.mobanisto.sqltools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MysqlUtil
{

	private static final Pattern patternBacktick = Pattern.compile("`(.*)`");

	public static String unpackBackticks(String name)
	{
		Matcher matcher = patternBacktick.matcher(name);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return name;
	}

}
