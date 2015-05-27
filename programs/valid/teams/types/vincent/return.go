/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func incr(x float64) float64 {
	return x + 1.0
}


type point struct {
	x, y, z float64
}

func new_point() point {
	var p point
	p.x = incr(-1.0)
	p.y = 0.0
	p.z = 0.0
	return p
}
