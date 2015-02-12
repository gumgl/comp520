William Bain / 260509251

Building
========

From the toplevel directory run `make -C src` (or equivalently run make from
src/). This should create the toplevel executable `mini`. The bash script
`run` invokes `mini` with the flags -p and -s to have it output pretty-
printed source and the symbol table.


Implementation notes
====================

The compiler will output invalid C source if variables are used which
conflict with standard C variables. For instance, it's possible to write a
valid Mini program using the variables main, scanf, printf, double, and so
on. It would be fairly easy to fix this by renaming all variables in the
C source (perhaps to var1, var2, and so on) but as the reference compiler
doesn't do so I haven't either.
