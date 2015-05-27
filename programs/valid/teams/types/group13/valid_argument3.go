/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*
 * Simple example of a function with void parameters, but a return type
 * Note that print is a polymorphic fucntion and can except a variety of types
 */
func test() float64 {
	return 5.5
}

func main() {
	print(test())
}

