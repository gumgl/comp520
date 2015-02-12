#ifndef MINILANG_SYMBOLTABLE_H
#define MINILANG_SYMBOLTABLE_H

#include <stdlib.h>

#include "tree.h"
#include "minitypes.h"

#define SYMBOL_TABLE_HASH_SIZE 317

typedef struct symbol_table_entry {
    struct symbol_table_entry *next;
    unsigned long int hash;
    char *name;
    mini_type type;
} symbol_table_entry;

typedef symbol_table_entry *symbol_table[SYMBOL_TABLE_HASH_SIZE];

int build_symbol_table(symbol_table, program *);
void free_symbol_table(symbol_table);

symbol_table_entry *lookup_symbol(symbol_table, char *);
void dump_symbol_table(FILE *, symbol_table);

#endif
