#include <stdio.h>

#include "minitypes.h"
#include "pretty.h"
#include "tree.h"

#define RETURN_ON_FAIL(f)   if ((f) != 0) return -1

/* Local function signatures */
int pretty_print_program(FILE *, program *);
int pretty_print_decls(FILE *, decl_node *, unsigned long);
int pretty_print_stmts(FILE *, stmt_node *, unsigned long);
int pretty_print_stmt(FILE *, stmt_node *, unsigned long);
int pretty_print_expr(FILE *, expr_node *);
void print_tabs(FILE *, unsigned long);


int pretty_print_program(FILE *out, program *prog) {
    RETURN_ON_FAIL(pretty_print_decls(out, prog->decl_block, 0));
    RETURN_ON_FAIL(pretty_print_stmts(out, prog->stmt_block, 0));
    return 0;
}

int pretty_print_decls(FILE *out, decl_node *decl, unsigned long nest_level) {
    char *type;

    while (decl != NULL) {
        switch (decl->type) {
        case INT_TYPE:
            type = "int";
            break;

        case FLOAT_TYPE:
            type = "float";
            break;

        default:
            fprintf(stderr, "Unrecognized type %d\n", decl->type);
            return -1;
        }

        print_tabs(out, nest_level);
        fprintf(out, "var %s : %s;\n", decl->id, type);
        decl = decl->next;
    }

    return 0;
}

int pretty_print_stmts(FILE *out, stmt_node *stmt, unsigned long nest_level) {
    while (stmt != NULL) {
        RETURN_ON_FAIL(pretty_print_stmt(out, stmt, nest_level));
        stmt = stmt->next;
    }

    return 0;
}

int pretty_print_stmt(FILE *out, stmt_node *stmt, unsigned long nest_level) {
    print_tabs(out, nest_level);

    switch (stmt->kind) {
    case IF_NODE:
        fprintf(out, "if ");
        RETURN_ON_FAIL(pretty_print_expr(out, stmt->val.one_block_ctrl.condition));
        fprintf(out, " then\n");

        RETURN_ON_FAIL(pretty_print_stmts(out, stmt->val.one_block_ctrl.block, nest_level+1));

        print_tabs(out, nest_level);
        fprintf(out, "endif\n");
        break;

    case WHILE_NODE:
        fprintf(out, "while ");
        RETURN_ON_FAIL(pretty_print_expr(out, stmt->val.one_block_ctrl.condition));
        fprintf(out, " do\n");

        RETURN_ON_FAIL(pretty_print_stmts(out, stmt->val.one_block_ctrl.block, nest_level+1));

        print_tabs(out, nest_level);
        fprintf(out, "done\n");
        break;

    case IF_ELSE_NODE:
        fprintf(out, "if ");
        RETURN_ON_FAIL(pretty_print_expr(out, stmt->val.two_block_ctrl.condition));
        fprintf(out, " then\n");

        RETURN_ON_FAIL(pretty_print_stmts(out, stmt->val.two_block_ctrl.block_one, nest_level+1));

        print_tabs(out, nest_level);
        fprintf(out, "else\n");
        RETURN_ON_FAIL(pretty_print_stmts(out, stmt->val.two_block_ctrl.block_two, nest_level+1));

        print_tabs(out, nest_level);
        fprintf(out, "endif\n");
        break;

    case ASSIGN_NODE:
        fprintf(out, stmt->val.assign.id);
        fprintf(out, " = ");
        RETURN_ON_FAIL(pretty_print_expr(out, stmt->val.assign.expr));
        fprintf(out, ";\n");
        break;

    case PRINT_NODE:
        fprintf(out, "print ");
        RETURN_ON_FAIL(pretty_print_expr(out, stmt->val.expr));
        fprintf(out, ";\n");
        break;

    case READ_NODE:
        fprintf(out, "read %s;\n", stmt->val.id);
        break;

    default:
        fprintf(stderr, "Failed to print statement of type %d\n", stmt->kind);
        return -1;
    }

    return 0;
}

int pretty_print_expr(FILE *out, expr_node *expr) {
    switch (expr->kind) {
    case ID_NODE:
        fprintf(out, expr->val.id);
        break;

    case INT_CONST_NODE:
        fprintf(out, "%d", expr->val.intconst);
        break;

    case FLOAT_CONST_NODE:
        fprintf(out, "%f", expr->val.floatconst);
        break;

    case MULT_NODE:
    case DIV_NODE:
    case PLUS_NODE:
    case MINUS_NODE:
        fputc('(', out);
        RETURN_ON_FAIL(pretty_print_expr(out, expr->val.binary.left));

        if (expr->kind == MULT_NODE)       fprintf(out, "*");
        else if (expr->kind == DIV_NODE)   fprintf(out, "/");
        else if (expr->kind == PLUS_NODE)  fprintf(out, "+");
        else if (expr->kind == MINUS_NODE) fprintf(out, "-");

        RETURN_ON_FAIL(pretty_print_expr(out, expr->val.binary.right));
        fputc(')', out);
        break;

    case UNARY_MINUS_NODE:
        fputc('-', out);
        fputc('(', out);
        RETURN_ON_FAIL(pretty_print_expr(out, expr->val.operand));
        fputc(')', out);
        break;

    default:
        fprintf(stderr, "Failed to print expression of type %d\n", expr->kind);
        return -1;
    }

    return 0;
}

void print_tabs(FILE *out, unsigned long nest_level) {
    for (unsigned long tabs=0; tabs < nest_level; tabs++) {
        fputc('\t', out);
    }
}
