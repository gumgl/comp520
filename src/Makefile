
# assumes sablecc script is on your PATH
.PHONY: grammar check clean

# to compile the code
main: clean grammar
	javac golite/*.java golite/*/*.java

# to generate the compiler code
grammar: golite.sablecc3
	sablecc golite.sablecc3

check:
	./check

clean:
	rm -rf golite/ results
