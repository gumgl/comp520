/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// cannot convert string to type rune

package main

func main() {
	s := "s"
	var x rune = rune(s)
	print(x)
}
