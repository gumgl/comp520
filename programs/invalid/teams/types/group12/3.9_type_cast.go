/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// cannot type cast something to alias of string
// note: this typechecks in Go

package main

type str string

func main() {
	var s str = str('a')
	print(s)
}
