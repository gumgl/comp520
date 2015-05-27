/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package short_variable_declare_lvalue

func bad() {
        a, a.b, a[b] := 1, 2, 3
}
