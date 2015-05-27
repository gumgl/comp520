/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/* 3.3 unary expressions*/
/* 3.4 binary expressions */

func main() {

	a := 8
	b := 2.0
	c := "lah"
	d := 'a'
	e := true

	println(e||e,e&&!e,c=="a", a<2, b<=3.0, c>"la", d>='b',"a"+"b",a-2,2*3,2%5,2|2,3&a, 1<<2,3<<2,2&^a, a^a)
	println(+2,+2.0,+'x')
	println(-2,-2.0,-'x')
	println(^2,^'x')
	println(!true,!false)
	
}
