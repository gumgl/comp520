/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func add(a int, b int) float64 {
	return float64(a + b);
}

func main() {	
	var a,b int
	// Invalid since one of the right-hand expressions does not typecheck
	a, b = add(1,2), 3 
}
