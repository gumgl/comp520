/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// type of x does not match type of 'a','b','c'

package main

func main() {
	x := true
	switch x {
	case 'a', 'b', 'c':
		print()
	}
}
