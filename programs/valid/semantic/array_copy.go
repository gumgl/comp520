/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

type container struct{ i int; }

func main() {
	var a [1]container
	a[0].i = 23

	var b = a
	b[0].i = 77
	
	println("Expected 23, got", a[0].i)
	println("Expected 77, got", b[0].i)
}
