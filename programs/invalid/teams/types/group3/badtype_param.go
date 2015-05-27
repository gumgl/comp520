/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func factorial(x int) int {
	var total int = 1;
	var iter int;
	for iter = 2; iter <= x; iter++ {
		total *= iter;
	} 
	return total;
}

func main() {
	// Call the factorial function with a bad parameter type
	var fac int = factorial(10.23)
	println(fac)
	return;
}
