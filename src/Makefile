SHELL := /bin/bash -O extglob
# assumes sablecc script is in your PATH
.PHONY: check clean

# to compile the code
mini: clean grammar
	javac *.java golite/*/*.java

# to generate the compiler code
grammar: golite.sablecc3
	sablecc golite.sablecc3

check:
	./check

# remove SableCC files, pretty.golite files and then anything not .min
clean:
	rm -rf *.class golite/* ../programs/*/*.pretty.go ../programs/*/!(*.go)
