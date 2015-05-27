/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main 

func main() {
	var x int = 100
	// The condition doesn't typecheck since it doesn't have a boolean type
	for x {
		println(x)
		x--
	}
}
