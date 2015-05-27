/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	b, c := 2, 3
	println(b, c)

	// No new decl on the lhs of a short decl
	b, c := 4, 5
	println(b, c)
}
