#ifndef MINILANG_TREE_H
#define MINILANG_TREE_H

#include "minitypes.h"

/* Kinds of expression node */
typedef enum {
  ID_NODE, INT_CONST_NODE, FLOAT_CONST_NODE, MULT_NODE, DIV_NODE,
  PLUS_NODE, MINUS_NODE, UNARY_MINUS_NODE
} expr_node_kind;

/* Kinds of statement node, including type declarations */
typedef enum {
  IF_NODE, IF_ELSE_NODE, WHILE_NODE, ASSIGN_NODE, READ_NODE, PRINT_NODE
} stmt_node_kind;

/* Struct for expression nodes */
typedef struct expr_node {
  expr_node_kind kind;
  mini_type mini_type;
  union {
    char *id;
    int intconst;
    float floatconst;
    struct {struct expr_node *left; struct expr_node *right;} binary;
    struct expr_node *operand;
  } val;
} expr_node;

/* Struct for statement nodes: contains a pointer to the next statement
 * and variables specific to the given kind of statement. */
typedef struct stmt_node {
  struct stmt_node *next;
  stmt_node_kind kind;

  union {
    char *id;
    expr_node *expr;

    struct {
      expr_node *condition;
      struct stmt_node *block;
    } one_block_ctrl;

    struct {
      expr_node *condition;
      struct stmt_node *block_one;
      struct stmt_node *block_two;
    } two_block_ctrl;

    struct {
      char *id;
      expr_node *expr;
    } assign;
  } val;
} stmt_node;

typedef struct decl_node {
  struct decl_node *next;
  char *id;
  mini_type type;
} decl_node;

/* Toplevel struct for the program, with separate blocks for type declarations
 * and for other statements */
typedef struct {
  decl_node *decl_block;
  stmt_node *stmt_block;
} program;

/* Program creation */
program *make_program(decl_node *decl, stmt_node *stmt);

/* Type declarations */
decl_node *make_decl(char *, mini_type);

/* Control-flow statements */
stmt_node *make_stmt_if(expr_node *condition, stmt_node *block);
stmt_node *make_stmt_if_else(expr_node *condition, stmt_node *block_one, stmt_node *block_two);
stmt_node *make_stmt_while(expr_node *condition, stmt_node *block);

/* Assignment statements */
stmt_node *make_stmt_assign(char *id, expr_node *assign_value);

/* I/O statements */
stmt_node *make_stmt_read(char *id);
stmt_node *make_stmt_print(expr_node *to_print);

/* Non-recursive expressions */
expr_node *make_expr_id(char *id);
expr_node *make_expr_int_const(int constant);
expr_node *make_expr_float_const(float constant);

/* Binary expressions */
expr_node *make_binary_expr(expr_node *left, expr_node *right, expr_node_kind kind);
expr_node *make_expr_mult(expr_node *left, expr_node *right);
expr_node *make_expr_div(expr_node *left, expr_node *right);
expr_node *make_expr_plus(expr_node *left, expr_node *right);
expr_node *make_expr_minus(expr_node *left, expr_node *right);

/* Unary negation */
expr_node *make_expr_unary_minus(expr_node *operand);

/* Memory deallocation */
void free_program(program*);

#endif /* !MINILANG_TREE_H */
