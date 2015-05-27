/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// int and rune are not comparable

package main

func main() {
	x, y := 1, '1'
	if x == y {
		print(y)
	}
}

