#include <stdlib.h>
#include <stdio.h>
#include "tree.h"
#include "malloc_safe.h"

/* Constructor functions */

/* Program */
program *make_program(decl_node *decl, stmt_node *stmt) {
    program *prog;
    prog = (program *)malloc_safe(sizeof(program));
    prog->decl_block = decl;
    prog->stmt_block = stmt;
    return prog;
}

/* Variable declarations */

decl_node *make_decl(char *id, mini_type type) {
    decl_node *decl;
    decl = (decl_node *)malloc_safe(sizeof(decl_node));
    decl->id = id;
    decl->type = type;
    return decl;
}

/* Statements (generic internal constructor) */
stmt_node *make_stmt(stmt_node_kind kind) {
    stmt_node *stmt;
    stmt = (stmt_node *)malloc_safe(sizeof(stmt_node));
    stmt->kind = kind;
    return stmt;
}

/* Control-flow statements */

stmt_node *make_stmt_if(expr_node *condition, stmt_node *block) {
    stmt_node *stmt;
    stmt = make_stmt(IF_NODE);
    stmt->val.one_block_ctrl.condition = condition;
    stmt->val.one_block_ctrl.block = block;
    return stmt;
}

stmt_node *make_stmt_if_else(expr_node *condition, stmt_node *block_one, stmt_node *block_two) {
    stmt_node *stmt;
    stmt = make_stmt(IF_ELSE_NODE);
    stmt->val.two_block_ctrl.condition = condition;
    stmt->val.two_block_ctrl.block_one = block_one;
    stmt->val.two_block_ctrl.block_two = block_two;
    return stmt;
}

stmt_node *make_stmt_while(expr_node *condition, stmt_node *block) {
    stmt_node *stmt;
    stmt = make_stmt(WHILE_NODE);
    stmt->val.one_block_ctrl.condition = condition;
    stmt->val.one_block_ctrl.block = block;
    return stmt;
}

/* Assignment statements */

/* Generic statement constructor (private) */
stmt_node *make_stmt_assign(char *id, expr_node *assign_value) {
    stmt_node *stmt;
    stmt = make_stmt(ASSIGN_NODE);
    stmt->val.assign.id = id;
    stmt->val.assign.expr = assign_value;
    return stmt;
}

/* I/O statements */

stmt_node *make_stmt_read(char *id) {
    stmt_node *stmt;
    stmt = make_stmt(READ_NODE);
    stmt->val.id = id;
    return stmt;
}

stmt_node *make_stmt_print(expr_node *to_print) {
    stmt_node *stmt;
    stmt = make_stmt(PRINT_NODE);
    stmt->val.expr = to_print;
    return stmt;
}

/* Expressions */

/* Generic expression constructor (private) */
expr_node *make_expr(expr_node_kind kind) {
    expr_node *expr;
    expr = (expr_node *)malloc_safe(sizeof(expr_node));
    expr->kind = kind;
    return expr;
}

/* Non-recursive expressions */

expr_node *make_expr_id(char *id) {
    expr_node *id_expr;
    id_expr = make_expr(ID_NODE);
    id_expr->val.id = id;
    return id_expr;
}

expr_node *make_expr_int_const(int constant) {
    expr_node *expr;
    expr = make_expr(INT_CONST_NODE);
    expr->val.intconst = constant;
    return expr;
}

expr_node *make_expr_float_const(float constant) {
    expr_node *expr;
    expr = make_expr(FLOAT_CONST_NODE);
    expr->val.floatconst = constant;
    return expr;
}

/* Binary expressions */

expr_node *make_binary_expr(expr_node *left, expr_node *right, expr_node_kind kind) {
    expr_node *bin;
    bin = make_expr(kind);
    bin->val.binary.left = left;
    bin->val.binary.right = right;
    return bin;
}

expr_node *make_expr_mult(expr_node *left, expr_node *right) {
    return make_binary_expr(left, right, MULT_NODE);
}

expr_node *make_expr_div(expr_node *left, expr_node *right) {
    return make_binary_expr(left, right, DIV_NODE);
}

expr_node *make_expr_plus(expr_node *left, expr_node *right) {
    return make_binary_expr(left, right, PLUS_NODE);
}

expr_node *make_expr_minus(expr_node *left, expr_node *right) {
    return make_binary_expr(left, right, MINUS_NODE);
}

/* Unary negation */

expr_node *make_expr_unary_minus(expr_node *operand) {
    expr_node *negated;
    negated = make_expr(UNARY_MINUS_NODE);
    negated->val.operand = operand;
    return negated;
}

/* Memory deallocation */
void free_decl_chain(decl_node *);
void free_stmt_chain(stmt_node *);
void free_stmt(stmt_node *);
void free_expr(expr_node *);

void free_program(program *prog) {
    free_decl_chain(prog->decl_block);
    free_stmt_chain(prog->stmt_block);
    free(prog);
}

void free_decl_chain(decl_node *decl) {
    decl_node *next;
    while (decl != NULL) {
        next = decl->next;
        free(decl->id);
        decl = next;
    }
}

void free_stmt_chain(stmt_node *stmt) {
    stmt_node *next;
    while (stmt != NULL) {
        next = stmt->next;
        free_stmt(stmt);
        stmt = next;
    }
}

void free_stmt(stmt_node *stmt) {
    switch (stmt->kind) {
    case IF_NODE:
    case WHILE_NODE:
        free_expr(stmt->val.one_block_ctrl.condition);
        free_stmt_chain(stmt->val.one_block_ctrl.block);
        break;

    case IF_ELSE_NODE:
        free_expr(stmt->val.two_block_ctrl.condition);
        free_stmt_chain(stmt->val.two_block_ctrl.block_one);
        free_stmt_chain(stmt->val.two_block_ctrl.block_two);
        break;

    case ASSIGN_NODE:
        free(stmt->val.assign.id);
        free_expr(stmt->val.assign.expr);
        break;

    case PRINT_NODE:
        free_expr(stmt->val.expr);
        break;

    case READ_NODE:
        free(stmt->val.id);
        break;

    default:
        fprintf(stderr, "Unexpected statement type %d\n", stmt->kind);
        abort();
    }

    free(stmt);
}

void free_expr(expr_node *expr) {
    switch (expr->kind) {
    case ID_NODE:
        free(expr->val.id);
        break;

    case INT_CONST_NODE:
    case FLOAT_CONST_NODE:
        /* Nothing special to deallocate */
        break;

    case MULT_NODE:
    case DIV_NODE:
    case PLUS_NODE:
    case MINUS_NODE:
        free_expr(expr->val.binary.left);
        free_expr(expr->val.binary.right);
        break;

    case UNARY_MINUS_NODE:
        free_expr(expr->val.operand);
        break;

    default:
        fprintf(stderr, "Unexpected expression type %d\n", expr->kind);
        abort();
    }

    free(expr);
}
