/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*2.7 Assignment*/

func main(){
	
	var x int = 0
	var y float64= 1.0
	var a,b int
	
	//error: not the same corresding type on both sides
	//cannot use y * y (type float64) as type int in assignment
	
	a,b = x*x , y*y
	println(a,b)
}
