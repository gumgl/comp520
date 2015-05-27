/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func f() int {
	return 0
}

func main() {
	// f() is an int, not a float64
	var x float64 = f()
}
