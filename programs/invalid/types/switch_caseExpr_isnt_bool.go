/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main () {

	var x int = 0
	switch x+=5;{
	
		//error: case expr has non-bool type 
		case x<1,x+2:
			println("x:",x)
		default:
			println("huh")
	}
	
}
