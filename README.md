GoLite Compiler
===============
COMP 520 Compiler Design, Winter 2015, Group 5

# Team members
- **Guillaume Labranche** 260585371
- **Will Bain** 260509251
- **Si Mei Zhang** 260400183

# Building

Make commands (from src/ directory):

~~~ sh
$ make
$ make clean
$ make check
$ make checkall
~~~

Running `make` creates the file `golite.jar` in the toplevel directory
as well as a Bash utility script named `golite`. If make is run under
Cygwin on a Windows machine, it also creates a Batch utility file named
`golite.bat`.

# Instructions

## Milestone 1

[Build](#building) the code as described above. From the root directory, run `golite -pprint inputFile`.

## Milestone 2

[Build](#building) the code as described above. From the root directory, run
`golite [-dumpsymtab] [-dumpsymtaball] [-pptype] inputFile`.
