/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

// This works if a new scope is opened for the switch statement as a whole
// and for each individual case
func main() {
	x := 1
	y := 2

	switch x := true; x {
	case true:
		y := "true"
		println("got", y)
	case false:
		y := "nothing worthwhile"
		println("got", y)
	default:
		y := "this is actually impossible"
		println(y)
	}
	println("meanwhile, x =", x, "and y =", y)
}
