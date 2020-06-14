package de.mobanisto.sqltools.php;

import java.util.ArrayList;
import java.util.List;

import de.mobanisto.sqltools.generator.BaseGenerator;

public class BasePhpGenerator extends BaseGenerator
{

	protected void phpClass(String className, String namespace,
			boolean commentGenerated)
	{
		phpClass(className, null, namespace, new ArrayList<String>(),
				commentGenerated);
	}

	protected void phpClass(String className, String superclass,
			String namespace, List<String> uses, boolean commentGenerated)
	{
		l("<?php");
		nl();
		l("namespace " + namespace + ";");
		nl();
		if (!uses.isEmpty()) {
			for (String use : uses) {
				l("use " + use + ";");
			}
			nl();
		}
		if (commentGenerated) {
			l("/** This file is generated, DO NOT EDIT */");
		}
		if (superclass == null) {
			l("class " + className);
		} else {
			l("class " + className + " extends " + superclass);
		}
		l("{");
		nl();
	}

}