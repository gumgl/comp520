/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	type slice []int
	type elem int

	var xs slice
	var ys []int
	var zs []elem

	var x elem
	var y int

	// xs = append(xs, x)
	xs = append(xs, y)

	// Cannot append a `elem` to a `[]int`
	ys = append(ys, x)
	ys = append(ys, y)

	zs = append(zs, x)
	// zs = append(zs, y)
}
