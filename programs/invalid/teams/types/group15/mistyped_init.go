/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

/* In this file, a type mismatch occurs in the short var decl */
package main

func main () {
	var x bool = true
	if x++; x {
		return
	}
}
