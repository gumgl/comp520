/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	type dog bool;
	var one dog;
	var two bool;
	
	switch one {
	case two:
		print("works");
	default:
		print("default");
	}
}
