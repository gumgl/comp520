/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// For op-assign statements, only a single id and single expression can be on either side of the assignment operator.

package main

func main() {
	var x, y int
	x, y = 10, 10
	if x>2 && y>2 {
		x, y -= 5
	}

}