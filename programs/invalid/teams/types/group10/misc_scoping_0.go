/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

type a int
type b a
type c b

type str struct {
	x, y int
}



func main() {

	var x int
	var v b = 3
	x = int(v)
	v = b(x) + 1
	println(x,v)

	var a int 
	b,a := 1,2
	println(a,b)

	v = b(x) + 1

}
