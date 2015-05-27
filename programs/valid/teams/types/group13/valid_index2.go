/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*
 * Indexing example testing both arrays and slices
 */
func main() {
	var a []int
	var b [10]int

	print(a[1])
	print(b[1])

	a[1] = 2
	b[1] = 2
}
