/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*Variable Declarations 1.1*/

func main(){
	var a int
	var b float64
	var c bool
	var d rune
	var e string
	a = 2
	b = 1.0
	c = true
	d = 'h'
	e = "huh"
	
	var f int = a * a
	var g = b
	println(a," ",b," ",c," ",d," ",e," ",f," ",g)
	{
	var b bool
	b = true
	println("redeclare of b in inner scope:", b)
	}
	//error: c redeclared in this block, 	previous declaration
	var c int
	c = 1
	println(c)
}
