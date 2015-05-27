/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func base_types() {
	var (
		a int
		b float64
		c rune
		d string
		e bool
	)

	// Unary plus
	println(+a)
	println(+b)
	println(+c)

	// Unary negation
	println(-a)
	println(-b)
	println(-c)

	// Unary bit-not
	println(^a)
	println(^c)

	// Unary logical not
	println(!e)
}

func type_aliases() {
	type (
		t1 int
		t2 float64
		t3 rune
		t4 string
		t5 bool
	)
	var (
		a t1
		b t2
		c t3
		d t4
		e t5
	)

	// Unary plus
	println(+a)
	println(+b)
	println(+c)

	// Unary negation
	println(-a)
	println(-b)
	println(-c)

	// Unary bit-not
	println(^a)
	println(^c)

	// Unary logical not
	println(!e)
}
