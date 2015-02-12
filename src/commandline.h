#ifndef MINILANG_COMMANDLINE_H
#define MINILANG_COMMANDLINE_H

#include <stdio.h>

typedef struct {
    char *fname;
    struct {
        unsigned int c       : 1;
        unsigned int pretty  : 1;
        unsigned int table   : 1;
        unsigned int invalid : 1;
    } flags;
} mini_options;

mini_options parse_mini_cmd_options(int argc, char **argv);

#endif
