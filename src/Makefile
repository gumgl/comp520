
# assumes sablecc script is on your PATH
.PHONY: check clean

# to compile the code
mini: clean grammar
	javac mini/*.java mini/lexer/*.java mini/parser/*.java mini/node/*.java mini/analysis/*.java 

# to generate the compiler code
grammar: mini.sablecc
	sablecc mini.sablecc

check:
	./check

clean:
	rm -rf mini/*.class mini/lexer/ mini/parser/ mini/node/ mini/analysis/ result
