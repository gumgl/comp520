/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

type point struct {
	x, y int
}

func three_four() point {
	var p point
	p.x = 3
	p.y = 4
	return p
}

func main() {
	println(three_four().x)
}
