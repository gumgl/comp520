
# assumes sablecc script is on your PATH
.PHONY: check clean

# to compile the code
main: clean grammar
	javac golite/*.java golite/*/*.java

# to generate the compiler code
grammar: golite.sablecc3
	sablecc golite.sablecc3

check:
	./check

clean:
	rm -rf golite/*.class golite/lexer/ golite/parser/ golite/node/ golite/analysis/ result
