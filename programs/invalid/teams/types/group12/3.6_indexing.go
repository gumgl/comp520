/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// left side of indexing expression must be array/slice

package main

func main() {
	type point struct {
		x, y int
	}
	var p point
	p[0] = 1
	print(p.x)
}
