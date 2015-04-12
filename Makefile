.PHONY: benchmarks

JOOS = ./joosc -O

benchmarks: joosa-src/joos
	$(JOOS) peepholebenchmarks/bench01/*.java
	$(JOOS) peepholebenchmarks/bench02/*.java
#	$(JOOS) peepholebenchmarks/bench03/*.java peepholebenchmarks/bench03/*/*.java
	$(JOOS) peepholebenchmarks/bench04/*.java
	$(JOOS) peepholebenchmarks/bench05/*.java
	$(JOOS) peepholebenchmarks/bench06/*.java
	$(JOOS) peepholebenchmarks/bench07/*.java

joosa-src/joos: joosa-src/patterns.h
	make -C joosa-src/
