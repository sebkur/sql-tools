#!/bin/bash


DIR=core/src/main/antlr/de/mobanisto/antlr/mysql
mkdir -p "$DIR"
wget -P "$DIR" https://github.com/antlr/grammars-v4/raw/master/sql/mysql/Positive-Technologies/MySqlLexer.g4
wget -P "$DIR" https://github.com/antlr/grammars-v4/raw/master/sql/mysql/Positive-Technologies/MySqlParser.g4
