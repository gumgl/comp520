/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	x :=0 
	switch x {
	case 1:
		println("x is 1")
		break
	default:
		println("x is something...")
	default:
		println("A second Default (this is wrong...)")
	}
}
