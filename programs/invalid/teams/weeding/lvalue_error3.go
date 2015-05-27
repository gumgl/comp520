/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// x*y is not a lvalue

package main;

func main() {
	var x,y int
	x*y = 5
	if x>2 {
		print()
	}

}