package de.mobanisto.sqltools.mapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

public class Mapping
{

	@Getter
	Map<String, TableMapping> includes = new HashMap<>();
	@Getter
	Map<String, TableMapping> mapped = new HashMap<>();
	@Getter
	Set<String> excludes = new HashSet<>();

}
