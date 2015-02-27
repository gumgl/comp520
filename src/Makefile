SHELL := /bin/bash -O extglob
# assumes sablecc script is in your PATH
.PHONY: check checkall clean

# to compile the code
mini: clean grammar
	javac *.java golite/*/*.java
	jar cvfe ../golite.jar Main *.class golite/*/!(*.java)

# create user utility scripts
../golite:
	../tools/make_user_scripts.sh ..

# to generate the compiler code
grammar: golite.sablecc3
	sablecc golite.sablecc3

check:
	./check
	
checkall:
	./checkall

# remove SableCC files, pretty.golite files and then anything not .min
clean:
	rm -rf *.class golite/* ../programs/*/*.pretty.go ../programs/*/!(*.go) ../golite.jar
