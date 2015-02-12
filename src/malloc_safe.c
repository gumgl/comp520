#include <stdlib.h>
#include <stdio.h>

void *malloc_safe(size_t n) {
    void *p;
    p = malloc(n);
    if (!p) {
        fprintf(stderr, "Memory allocation failed\n");
        fflush(stderr);
        abort();
    }
    return p;
}
