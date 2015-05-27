/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// mismatched types double and int

package main

type double float64

func main() {
	var x double = 2.2
	y := 1
	print(x^y)
}

