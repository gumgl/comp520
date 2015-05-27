/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main 

func main() {
	var x int
	// The clause doesn't type check because the post expression doesn't type check
	for x = 10; x > 0; x -= 1.0 {
		println(x)
	}
}
