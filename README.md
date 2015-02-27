GoLite Compiler
===============
COMP 520 Compiler Design, Winter 2015, Group 5

# Team members
- **Guillaume Labranche** 
- **Will Bain** 
- **Si Mei Zhang** 

# Building

Make commands (from src/ directory):

~~~ sh
$ make
$ make clean
$ make check
$ make checkall
~~~

Running `make` creates the file `golite.jar` in the toplevel directory
as well as Bash and Windows batch file utility scripts named `golite` and
`golite.bat`.

# Instructions

These are instructions to invoke the behavior specified for the
given milestone with the code in this revision, after having built
the code as specified above.

## Milestone 1

From the root directory, run `golite -pprint inputFile`.

## Milestone 2

From the root directory, run
`golite [-dumpsymtab] [-dumpsymtaball] [-pptype] inputFile`.
