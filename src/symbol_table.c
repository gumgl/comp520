#include <stdio.h>
#include <string.h>

#include "symbol_table.h"
#include "minitypes.h"
#include "tree.h"
#include "malloc_safe.h"

int register_declarations(symbol_table, decl_node *);
int enter_symbol(symbol_table, char *, mini_type);
unsigned long int get_hash(char *);


int build_symbol_table(symbol_table table, program *prog) {
    return register_declarations(table, prog->decl_block);
}

void dump_symbol_table(FILE *out, symbol_table table) {
    int i;
    symbol_table_entry *entry;

    for (i = 0; i < SYMBOL_TABLE_HASH_SIZE; i++) {
        entry = table[i];
        while (entry != NULL) {
            fprintf(out, "%s: %s\n", entry->name, (entry->type == INT_TYPE ? "int" : "float"));
            entry = entry->next;
        }
    }
}

void free_symbol_table(symbol_table table) {
    int i;
    symbol_table_entry *entry, *next;

    for (i = 0; i < SYMBOL_TABLE_HASH_SIZE; i++) {
        entry = table[i];
        while (entry != NULL) {
            next = entry->next;
            free(entry);
            entry = next;
        }
        table[i] = NULL;
    }
}

int register_declarations(symbol_table table, decl_node *decl) {
    while (decl != NULL) {
        if (enter_symbol(table, decl->id, decl->type) != 0) {
            return -1;
        }
        decl = decl->next;
    }
    return 0;
}

int enter_symbol(symbol_table table, char *symbol, mini_type type) {
    unsigned long int hash = get_hash(symbol);
    symbol_table_entry *previous, *current, *new_entry;

    previous = NULL;
    current = table[hash % SYMBOL_TABLE_HASH_SIZE];

    while (current != NULL) {
        if (current->hash == hash && strcmp(current->name, symbol) == 0) {
            fprintf(stderr, "The variable `%s' is already declared\n", symbol);
            return -1;
        }
        previous = current;
        current = current->next;
    }

    new_entry = (symbol_table_entry *)malloc_safe(sizeof(symbol_table_entry));

    new_entry->hash = hash;
    new_entry->name = symbol;
    new_entry->type = type;
    new_entry->next = NULL;

    if (previous == NULL) {
        table[hash % SYMBOL_TABLE_HASH_SIZE] = new_entry;
    } else {
        previous->next = new_entry;
    }

    return 0;
}

symbol_table_entry *lookup_symbol(symbol_table table, char *symbol) {
    unsigned long int hash = get_hash(symbol);
    symbol_table_entry *current = table[hash % SYMBOL_TABLE_HASH_SIZE];

    while (current != NULL) {
        if (current->hash == hash && strcmp(current->name, symbol) == 0) {
            return current;
        }
        current = current->next;
    }

    return NULL;
}

unsigned long int get_hash(char *symbol) {
    unsigned long int hash = 0;
    char c;

    for (int i = 0; (c = symbol[i]) != '\0'; i++) {
        hash = (hash << 1) + c;
    }

    return hash;
}
