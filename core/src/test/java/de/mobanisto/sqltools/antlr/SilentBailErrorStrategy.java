package de.mobanisto.sqltools.antlr;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;

public class SilentBailErrorStrategy extends BailErrorStrategy
{

	@Override
	public void reportError(Parser recognizer, RecognitionException e)
	{
		// do nothing
	}

}
