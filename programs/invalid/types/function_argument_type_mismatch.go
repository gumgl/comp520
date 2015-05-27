/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func adder (a int, b float64) int{
	var c = a+int(b)
	print(c)
	return c
}

func main(){

	//cannot use "a" (type string) as type float64 in argument to adder
	adder(2,"a")
}
