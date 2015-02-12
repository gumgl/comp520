#include <stdio.h>

#include "transpile.h"
#include "tree.h"
#include "minitypes.h"

void transpile_program(FILE *out, program *prog);
void transpile_decls(FILE *, decl_node *);
void transpile_stmts(FILE *, stmt_node *);
void transpile_stmt(FILE *, stmt_node *);
void transpile_expr(FILE *out, expr_node *expr);

void transpile_program(FILE *out, program *prog) {
    fprintf(out, "#include <stdio.h>\nint main(void) {\n");
    transpile_decls(out, prog->decl_block);
    transpile_stmts(out, prog->stmt_block);
    fprintf(out, "return 0;\n}\n");
}

void transpile_decls(FILE *out, decl_node *decl) {
    while (decl != NULL) {
        fprintf(out, "%s %s;\n", (decl->type == INT_TYPE ? "int" : "float"), decl->id);
        decl = decl->next;
    }
}

void transpile_stmts(FILE *out, stmt_node *stmt) {
    while (stmt != NULL) {
        transpile_stmt(out, stmt);
        stmt = stmt->next;
    }
}

void transpile_stmt(FILE *out, stmt_node *stmt) {
    switch (stmt->kind) {
    case IF_NODE:
        fprintf(out, "if (");
        transpile_expr(out, stmt->val.one_block_ctrl.condition);
        fprintf(out, ") {\n");
        transpile_stmts(out, stmt->val.one_block_ctrl.block);
        fprintf(out, "}\n");
        break;

    case WHILE_NODE:
        fprintf(out, "while (");
        transpile_expr(out, stmt->val.one_block_ctrl.condition);
        fprintf(out, ") {\n");
        transpile_stmts(out, stmt->val.one_block_ctrl.block);
        fprintf(out, "}\n");
        break;

    case IF_ELSE_NODE:
        fprintf(out, "if (");
        transpile_expr(out, stmt->val.two_block_ctrl.condition);
        fprintf(out, ") {\n");
        transpile_stmts(out, stmt->val.two_block_ctrl.block_one);
        /* Special case: help the readability of if-else constructs by not wrapping in brackets */
        if (stmt->val.two_block_ctrl.block_two != NULL && stmt->val.two_block_ctrl.block_two->next == NULL &&
            (stmt->val.two_block_ctrl.block_two->kind == IF_NODE || stmt->val.two_block_ctrl.block_two->kind == IF_ELSE_NODE)) {
            fprintf(out, "} else ");
            transpile_stmts(out, stmt->val.two_block_ctrl.block_two);
        } else {
            fprintf(out, "} else {\n");
            transpile_stmts(out, stmt->val.two_block_ctrl.block_two);
            fprintf(out, "}\n");
        }
        break;

    case ASSIGN_NODE:
        fprintf(out, stmt->val.assign.id);
        fprintf(out, " = ");
        transpile_expr(out, stmt->val.assign.expr);
        fprintf(out, ";\n");
        break;

    case PRINT_NODE:
        fprintf(out, "printf(\"%%%c\\n\", ", (stmt->val.expr->mini_type == INT_TYPE ? 'd' : 'f'));
        transpile_expr(out, stmt->val.expr);
        fprintf(out, ");\n");
        break;

    case READ_NODE:
        fprintf(out, "scanf(\"%%%c\", &%s);\n", (stmt->val.expr->mini_type == INT_TYPE ? 'd' : 'f'), stmt->val.id);
        break;

    default:
        fprintf(stderr, "Failed to transpile statement of type %d\n", stmt->kind);
    }
}

void transpile_expr(FILE *out, expr_node *expr) {
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
        transpile_expr(out, expr->val.binary.left);

        if (expr->kind == MULT_NODE)       fprintf(out, "*");
        else if (expr->kind == DIV_NODE)   fprintf(out, "/");
        else if (expr->kind == PLUS_NODE)  fprintf(out, "+");
        else if (expr->kind == MINUS_NODE) fprintf(out, "-");

        transpile_expr(out, expr->val.binary.right);
        fputc(')', out);
        break;

    case UNARY_MINUS_NODE:
        fputc('-', out);
        fputc('(', out);
        transpile_expr(out, expr->val.operand);
        fputc(')', out);
        break;

    default:
        fprintf(stderr, "Failed to transpile expression of type %d\n", expr->kind);
    }
}
