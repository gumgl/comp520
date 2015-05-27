/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	// Here's a basic `switch`.
	i := 2
	print("write ", i, " as ")
	switch i {
	case 1:
		println("one")
	case 2:
		println("two")
	case 3:
		println("three")
	}
}
