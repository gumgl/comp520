/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*
 * Valid example of field selection
 */
func main() {
	type test2 struct {
		x string
	}

	type test struct {
		a int
		b int
		c float64
		d [5]int
		e test2
	}

	var t test;

	print(t.a, t.b, t.c, t.d[1], t.e.x)
}
