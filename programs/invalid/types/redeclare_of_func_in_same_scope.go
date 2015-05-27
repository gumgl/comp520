/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main(){

	adder(2,3)
}



func adder (a int, b int) {
	var c = a+b
	print(c)
}

//error: adder redeclared in this block, previous declaration at
func adder (a int, b int) {
	var c = a+b+b
	print (c)
}
