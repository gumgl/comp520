/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func mult(a float64, b float64) float64 {
	var result float64 = a * b

	// Invalid since the function returns an int while it return a float64
	return 0
}

func main() {
		// Invalid since the - operator expects a numeric type
    println(mult(5.0,6.5))
}
