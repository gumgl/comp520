CC=gcc
CFLAGS=-std=c99

DEBUG ?= 0
ifeq ($(DEBUG), 1)
	CFLAGS += -g -pedantic -Wall
endif

OBJ=main.o mini.tab.o lex.yy.o tree.o malloc_safe.o commandline.o pretty.o symbol_table.o typecheck.o transpile.o
DEPS=mini.tab.h tree.h malloc_safe.h commandline.h pretty.h symbol_table.h typecheck.h transpile.h
BIN=../mini

.PHONY: clean

main: $(OBJ) $(DEPS)
	$(CC) $(CFLAGS) $(OBJ) -lfl -o $(BIN)

main.c: commandline.h tree.h symbol_table.h pretty.h typecheck.h transpile.h malloc_safe.h mini.tab.h

commandline.c: commandline.h

tree.c: tree.h malloc_safe.h

pretty.c: pretty.h tree.h

symbol_table.c: symbol_table.h minitypes.h tree.h malloc_safe.h

typecheck.c: typecheck.h symbol_table.h minitypes.h tree.h

transpile.c: transpile.h minitypes.h tree.h

malloc_safe.c: malloc_safe.h

mini.tab.c mini.tab.h: mini.y tree.h minitypes.h
	bison -d mini.y

lex.yy.c: mini.l mini.tab.h malloc_safe.h
	flex mini.l

clean:
	rm mini.tab.c mini.tab.h; rm lex.yy.c; rm *.o; rm $(BIN)
