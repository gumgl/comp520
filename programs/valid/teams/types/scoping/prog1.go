/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

type point struct {
	x, y, z float64
}

func main() {
	var p point
	var y float64
	{
		type p int
		var x p = p(3)
		var y p = x + x
		println(y)
	}
	p.y = y
}
