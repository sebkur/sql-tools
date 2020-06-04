package de.mobanisto.sqltools.php;

import lombok.Getter;

public class ClassResult
{

	@Getter
	private String className;
	@Getter
	private String content;

	public ClassResult(String className, String content)
	{
		this.className = className;
		this.content = content;
	}

}
