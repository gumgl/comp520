/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*
 * Tests append type checking
 * first argument is of type Slice<T> or Array<T> (or alias) and second
 * argument is of type T
 */
func main() {
	var a []int
	append(a, 1)
}
