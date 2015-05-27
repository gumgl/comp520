/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main 

func main() {
	// The clause doesn't type check because the init expression doesn't type check
	if x:= 0.0; x > 1 {
		println("Bigger than one!")
	} else {
		println("Smaller or equal to one!")
	}
}
