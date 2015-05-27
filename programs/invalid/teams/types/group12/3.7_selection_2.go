/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// point does not have a field named z
package main

type point struct {
	x, y float64
}

func main() {
	var p point
	p.z = 1.0
	print(p.z)
}
