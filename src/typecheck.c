#include <stdio.h>

#include "typecheck.h"
#include "tree.h"
#include "minitypes.h"
#include "symbol_table.h"

int typecheck_stmts(symbol_table, stmt_node *);
int typecheck_stmt(symbol_table, stmt_node *);
int typecheck_assign_stmt(symbol_table, stmt_node *);
int typecheck_expr(symbol_table, expr_node *);
int typecheck_conditional(symbol_table, expr_node *);

int typecheck_program(symbol_table table, program *prog) {
    return typecheck_stmts(table, prog->stmt_block);
}

int typecheck_stmts(symbol_table table, stmt_node *stmt) {
    while (stmt != NULL) {
        if (typecheck_stmt(table, stmt) != 0) return -1; /* Should this fail immediately? */
        stmt = stmt->next;
    }

    return 0;
}

int typecheck_stmt(symbol_table table, stmt_node *stmt) {
    switch (stmt->kind) {
    case IF_NODE:
    case WHILE_NODE:
        if (typecheck_conditional(table, stmt->val.one_block_ctrl.condition) != 0) return -1;
        return typecheck_stmts(table, stmt->val.one_block_ctrl.block);

    case IF_ELSE_NODE:
        if (typecheck_conditional(table, stmt->val.two_block_ctrl.condition) != 0) return -1;
        if (typecheck_stmts(table, stmt->val.two_block_ctrl.block_one) != 0) return -1;
        return typecheck_stmts(table, stmt->val.two_block_ctrl.block_two);

    case ASSIGN_NODE:
        return typecheck_assign_stmt(table, stmt);

    case PRINT_NODE:
        return typecheck_expr(table, stmt->val.expr);

    case READ_NODE:
        /* FIXME: it should probably store the lookup type, right? */
        if (lookup_symbol(table, stmt->val.id) == NULL) {
            fprintf(stderr, "Variable `%s' not defined\n", stmt->val.id);
            return -1;
        }
        return 0;

    default:
        fprintf(stderr, "Unexpected statement type %d\n", stmt->kind);
        return -1;
    }

    /* Unreachable */
    return -1;
}

int typecheck_assign_stmt(symbol_table table, stmt_node *stmt) {
    symbol_table_entry *symbol_info;
    expr_node *expr;

    /* FIXME: it should probably store the lookup type, right? */
    symbol_info = lookup_symbol(table, stmt->val.assign.id);
    if (symbol_info == NULL) {
        fprintf(stderr, "Cannot assign to undeclared variable `%s'\n", stmt->val.assign.id);
        return -1;
    }

    expr = stmt->val.assign.expr;
    if (typecheck_expr(table, expr) != 0) return -1;

    /* This is the only impermissible assignment type combination */
    if (symbol_info->type == INT_TYPE && expr->mini_type == FLOAT_TYPE) {
        fprintf(stderr, "Cannot assign a float value to an integer variable\n");
        return -1;
    }
    return 0;
}

int typecheck_conditional(symbol_table table, expr_node *expr) {
    if (typecheck_expr(table, expr) != 0) return -1;

    if (expr->mini_type != INT_TYPE) {
        fprintf(stderr, "Illegal type in integer expression\n");
        return -1;
    }

    return 0;
}

int typecheck_expr(symbol_table table, expr_node *expr) {
    symbol_table_entry *entry;

    switch (expr->kind) {
    case ID_NODE:
        entry = lookup_symbol(table, expr->val.id);
        if (entry == NULL) {
            fprintf(stderr, "Referenced variable `%s' is undeclared\n", expr->val.id);
            return -1;
        }
        expr->mini_type = entry->type;
        return 0;

    case INT_CONST_NODE:
        expr->mini_type = INT_TYPE;
        return 0;

    case FLOAT_CONST_NODE:
        expr->mini_type = FLOAT_TYPE;
        return 0;

    case MULT_NODE:
    case DIV_NODE:
    case PLUS_NODE:
    case MINUS_NODE:
        if (typecheck_expr(table, expr->val.binary.left) != 0) return -1;
        if (typecheck_expr(table, expr->val.binary.right) != 0) return -1;
        if (expr->val.binary.left->mini_type == INT_TYPE && expr->val.binary.right->mini_type == INT_TYPE) {
            expr->mini_type = INT_TYPE;
        } else {
            expr->mini_type = FLOAT_TYPE;
        }
        return 0;

    case UNARY_MINUS_NODE:
        if (typecheck_expr(table, expr->val.operand) != 0) return -1;
        expr->mini_type = expr->val.operand->mini_type;
        return 0;

    default:
        fprintf(stderr, "Unexpected expression type %d\n", expr->kind);
        return -1;
    }
}
