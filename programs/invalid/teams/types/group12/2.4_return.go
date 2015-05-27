/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// f has a return type but return statement has no expression

package main

func f() int {
	return
}

func main() {
	print(f())
}
