/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main () {

	var x int = 0
	switch x+=5;x{
		case 1,2,3,4:
			println("x:",x)
			
		//invalid case true in switch on x (mismatched types bool and int)
		case true:
			println("it is",5)
		default:
			println("huh")
	}
	
}
