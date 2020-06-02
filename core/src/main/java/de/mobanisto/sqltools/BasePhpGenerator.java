package de.mobanisto.sqltools;

public class BasePhpGenerator extends BaseGenerator
{

	protected void phpClass(String className, boolean commentGenerated)
	{
		l("<?php");
		nl();
		if (commentGenerated) {
			l("/** This file is genereated, DO NOT EDIT */");
		}
		l("class " + className);
		l("{");
		nl();
	}

}