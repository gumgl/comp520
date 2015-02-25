SHELL := /bin/bash -O extglob
# assumes sablecc script is in your PATH
.PHONY: check checkall clean

# to compile the code
*.class: *.java golite/*/*.java
	javac *.java golite/*/*.java

# to generate the compiler code
golite/*/*.java: golite.sablecc3
	make clean
	sablecc golite.sablecc3

check:
	./check
	
checkall:
	./checkall

# remove SableCC files, pretty.golite files and then anything not .go
clean:
	rm -rf *.class golite/analysis golite/lexer golite/node golite/parser
