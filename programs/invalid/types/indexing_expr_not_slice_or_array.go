/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {

	//array
	var a [5]int
	a[4] = 100
	
	//slice
	var b [] int
	var c int
	
	//indexing ok
	b[1] = 100
	
	//error, c is not array nor slice
	c[1] = 100 

}
