/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

type person struct {
	name string
	age int
}

func main() {
	// This doesn't type check because a struct cannot be typecast
	var bob person
	println(int(bob))
}
