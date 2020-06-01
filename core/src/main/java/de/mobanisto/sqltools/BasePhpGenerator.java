package de.mobanisto.sqltools;

public class BasePhpGenerator extends BaseGenerator
{

	protected void phpClass(String className)
	{
		l("<?php");
		nl();
		l("class " + className);
		l("{");
		nl();
	}

}