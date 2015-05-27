/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	a := 1
	b := a
	println("ints:", a == b)
	println("inequality:", a != b)

	c := "xyz"
	d := "xy"
	println("strings:", c == d+"z")

	var e, f [1]int;
	println("arrays:", e == f)

	var g, h struct { x int; }
	println("structs:", g == h)
    println("struct inequality:", g != h)
}
