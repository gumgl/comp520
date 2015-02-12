#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "commandline.h"
#include "tree.h"
#include "symbol_table.h"
#include "pretty.h"
#include "typecheck.h"
#include "transpile.h"
#include "malloc_safe.h"
#include "mini.tab.h"

/* flex global */
FILE *yyin;

/* local helper functions */
void output_pretty_printed(const char *, program *);
void output_symbol_table(const char *, symbol_table);
void output_c_code(const char *, program *);
FILE *open_file_with_ext(const char *, const char *);

int main(int argc, char **argv) {
    mini_options opts;
    FILE *infile;
    program *full_ast;
    symbol_table table = {0};
    int symbol_table_success;

    full_ast = NULL;
    infile = NULL;

    /* Parse command-line options */
    opts = parse_mini_cmd_options(argc, argv);
    if (opts.flags.invalid) goto fail;

    /* Open the input file (if specified) */
    if (opts.fname != NULL) {
        infile = fopen(opts.fname, "r");
        if (infile == NULL) {
            perror("mini");
            goto fail;
        }
        yyin = infile;
    }

    /* Parse the AST */
    if (yyparse(&full_ast) != 0) goto fail;

    if (infile != NULL) {
        fclose(infile);
        infile = NULL;
    }

    /* Pretty-print the AST if specified */
    if (opts.fname != NULL && opts.flags.pretty) {
        output_pretty_printed(opts.fname, full_ast);
    }

    /* Build the symbol table */
    symbol_table_success = build_symbol_table(table, full_ast);
    if (opts.fname != NULL && opts.flags.table) {
        output_symbol_table(opts.fname, table);
    }
    if (symbol_table_success != 0) goto fail;
    if (typecheck_program(table, full_ast) != 0) goto fail;

    if (opts.flags.c) {
        if (opts.fname != NULL) {
            output_c_code(opts.fname, full_ast);
        } else {
            transpile_program(stdout, full_ast);
        }
    }

    free_symbol_table(table);
    free_program(full_ast);

    return EXIT_SUCCESS;

fail:
    if (infile != NULL) {
        fclose(infile);
    }

    if (full_ast != NULL) {
        free_symbol_table(table);
        free_program(full_ast);
    }

    return EXIT_FAILURE;
}

void output_pretty_printed(const char *input_fname, program *prog) {
    FILE *pretty_file;
    pretty_file = open_file_with_ext(".pretty.min", input_fname);
    if (pretty_file != NULL) {
        pretty_print_program(pretty_file, prog);
        fclose(pretty_file);
    }
}

void output_symbol_table(const char *input_fname, symbol_table table) {
    FILE *table_file;
    table_file = open_file_with_ext(".symbol.txt", input_fname);
    if (table_file != NULL) {
        dump_symbol_table(table_file, table);
        fclose(table_file);
    }
}

void output_c_code(const char *input_fname, program *prog) {
    FILE *c_file;

    c_file = open_file_with_ext(".c", input_fname);
    if (c_file != NULL) {
        transpile_program(c_file, prog);
        fclose(c_file);
    }
}

/* Replace the extension and open the new file in write mode */
FILE *open_file_with_ext(const char *new_ext, const char *input_fname) {
    size_t input_fname_length, new_ext_length;
    char *fname;
    FILE *new_file;

    new_ext_length = strlen(new_ext);
    input_fname_length = strlen(input_fname);

    /* If the old file name ends with .min, then replace it.
       Otherwise, just append the new extension to the existing name. */
    if (input_fname_length > 4 &&
        input_fname[input_fname_length-4] == '.' &&
        input_fname[input_fname_length-3] == 'm' &&
        input_fname[input_fname_length-2] == 'i' &&
        input_fname[input_fname_length-1] == 'n') {

        fname = (char *)malloc_safe(input_fname_length - 4 + new_ext_length + 1);
        strncpy(fname, input_fname, input_fname_length - 4);
        strcpy(fname + (input_fname_length-4), new_ext);
    } else {
        fname = (char *)malloc_safe(input_fname_length + new_ext_length + 1);
        strcpy(fname, input_fname);
        strcpy(fname + input_fname_length, new_ext);
    }

    new_file = fopen(fname, "w");
    free(fname);

    if (new_file == NULL) {
        perror("mini");
    }

    return new_file;
}
