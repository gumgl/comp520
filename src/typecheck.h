#ifndef MINILANG_TYPECHECK_H
#define MINILANG_TYPECHECK_H

#include "tree.h"
#include "symbol_table.h"

int typecheck_program(symbol_table, program *);

#endif
