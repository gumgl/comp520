SHELL := /bin/bash -O extglob
# assumes sablecc script is in your PATH
.PHONY: check checkall clean

# to compile the code
*.class: *.java golite/*/*.java
	javac *.java golite/*/*.java
	jar cvfe ../golite.jar Main *.class golite/*/!(*.java)

# create user utility scripts
../golite:
	../tools/make_user_scripts.sh ..

# to generate the compiler code
golite/*/*.java: golite.sablecc3
	make clean
	sablecc golite.sablecc3

check:
	./check
	
checkall:
	./checkall

# remove .class files, SableCC files, and generated files in the programs
# directory
clean:
	rm -rf *.class golite/analysis golite/lexer golite/node golite/parser ../golite.jar ../golite ../golite.bat
	git clean -f ../programs
