SHELL := /bin/bash -O extglob
# assumes sablecc script is in your PATH

.PHONY: check checkall clean main

sablecc_generated = golite/parser golite/analysis golite/node golite/lexer
java_source = golite/*.java golite/**/*.java
java_class = golite/*.class golite/**/*.class
jar_resource = golite/**/*.dat

main: ../golite.jar ../golite

# create user utility scripts
../golite:
	cd ..; tools/make_user_scripts.sh

# Create jarfile
../golite.jar: $(java_class) $(sablecc_generated)
	jar cfe ../golite.jar golite.Main $(java_class) $(jar_resource)

$(java_class): $(java_source) $(sablecc_generated)
	javac $(java_source)

# to generate the compiler code
$(sablecc_generated): golite.sablecc3
	make clean
	sablecc golite.sablecc3

check:
	./check
	
checkall:
	./checkall

# remove .class files, SableCC files, and generated files in the programs
# directory
clean:
	rm -rf $(sablecc_generated) *.class ../golite.jar ../golite ../golite.bat
	git clean -fX ../programs
