package de.mobanisto.sqltools;

public class BasePhpGenerator extends BaseGenerator
{

	protected void phpClass(String className, String namespace,
			boolean commentGenerated)
	{
		l("<?php");
		nl();
		l("namespace " + namespace + ";");
		nl();
		if (commentGenerated) {
			l("/** This file is genereated, DO NOT EDIT */");
		}
		l("class " + className);
		l("{");
		nl();
	}

}