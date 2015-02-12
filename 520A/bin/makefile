SHELL := /bin/bash -O extglob
# assumes sablecc script is in your PATH
.PHONY: check clean

# to compile the code
mini: clean grammar
	javac *.java mini/*/*.java 

# to generate the compiler code
grammar:
	sablecc mini.sablecc3

check:
	./check

# remove SableCC files, pretty.min files and then anything not .min
clean:
	rm -rf *.class mini/* ../programs/*/*.pretty.min ../programs/*/!(*.min)
