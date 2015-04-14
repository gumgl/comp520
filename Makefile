.PHONY: benchmarks

JOOS = ./joosc -O

benchmarks: JOOSA-src/joos
	$(JOOS) PeepholeBenchmarks/bench01/*.java
	$(JOOS) PeepholeBenchmarks/bench02/*.java
#	$(JOOS) PeepholeBenchmarks/bench03/*.java PeepholeBenchmarks/bench03/*/*.java
	$(JOOS) PeepholeBenchmarks/bench04/*.java
	$(JOOS) PeepholeBenchmarks/bench05/*.java
	$(JOOS) PeepholeBenchmarks/bench06/*.java
	$(JOOS) PeepholeBenchmarks/bench07/*.java

JOOSA-src/joos: JOOSA-src/patterns.h
	make -C JOOSA-src/



