# SQL tools

A number of tools I need in day to day work that is concerned with extracting
useful information from SQL dumps.

## Generic

The `drop-inserts` tools drops all INSERT statements from a dump.
Useful when working with large dumps and you want to focus on CREATE
statements or anything where all the real data is just in the way.

## PHP model classes

The following tools generate PHP model classes for an existing SQL schema:

* `display-php-model-mapping`
* `generate-php-model`
* `generate-php-table-model`

## Wikimedia dumps

The `wikimedia-extract-id-and-title` tools extracts page id and title from
a Wikimedia dump file. The input is a Wikipedia dump
(such as LLwiki-YYYYMMDD-page.sql.gz) and the output is a simple SQLite
database with a single table containing two columns: `id` and `title`.
It is configured to drop everything that is not in the main namespace
(namespace id = 0).

If you want to work efficiently with the resulting database, it can make sense
to create some indexes:

    CREATE INDEX ids on pages(id);
    CREATE INDEX titles on pages(title);

On dump file `dewiki-20200720-page.sql.gz` which has 250Mb of compressed data,
the resulting SQLite database has 120Mb, adding the `ids` index bumps it up to
167Mb and adding the `titles` index bumps it up to 272Mb.
