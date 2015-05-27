/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	// In go this fails; we need to typecheck the expressions
	// before adding the new variables to the symbol table
	a, b, c := 1, a+1, b+1
}
