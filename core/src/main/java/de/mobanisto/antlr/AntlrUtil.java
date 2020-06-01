package de.mobanisto.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

public class AntlrUtil
{

	public static String getText(CharStream charStream, ParserRuleContext ctx)
	{
		Interval interval = new Interval(ctx.start.getStartIndex(),
				ctx.stop.getStopIndex());
		return charStream.getText(interval);
	}

}
