/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main 

func assign_stmts() {
	var c [2]int
	var point struct { 
		x float64
	}
	c[0], point.x = 3, 3.1415; 
}
