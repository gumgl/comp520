/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// type of "str" doesn't match return type of f

package main

func f() rune {
	return "str"
}

func main() {
	print(f())
}
