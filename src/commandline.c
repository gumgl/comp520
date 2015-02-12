#include <stdio.h>
#include <unistd.h>
#include <ctype.h>
#include <getopt.h>
#include "commandline.h"

void print_usage(const char *prog_name);

mini_options parse_mini_cmd_options(int argc, char **argv) {
    int c;
    mini_options opts;

    opts.fname = NULL;
    opts.flags.c = 1;
    opts.flags.pretty = 0;
    opts.flags.table = 0;
    opts.flags.invalid = 0;

    while (((c = getopt(argc, argv, "psdf:")) != -1)) {
        switch (c) {
        case 'p':
            opts.flags.pretty = 1;
            break;

        case 's':
            opts.flags.table = 1;
            break;

        case 'd':
            opts.flags.c = 0;
            break;

        case 'f':
            opts.fname = optarg;
            break;

        default:
            print_usage(argv[0]);
            opts.flags.invalid = 1;
            return opts;
        }
    }

    /* Validate */
    if (opts.fname == NULL && (opts.flags.pretty || opts.flags.table)) {
        fprintf(stderr, "mini: cannot output extra files without an input file specified\n");
        print_usage(argv[0]);
        opts.flags.invalid = 1;
    }

    return opts;
}

void print_usage(const char *prog_name) {
    fprintf(
        stderr,
        "Usage: %s [-p] [-s] [-d] [-f filename]\n\n"
        "Arguments:\n"
        "    -f filename: Read from the file (defaults to stdin)\n"
        "    -p: Pretty print the input to [filename].pretty.min\n"
        "    -s: Write the symbol table to [filename].symbol.txt\n"
        "    -d: Dry run; do not write a transpiled C program to [filename].c\n\n"
        "The -p and -s options are only allowed if an input file is specified.\n"
        "If no input is specified then the C output is written to stdout.\n",
        prog_name
    );
}

