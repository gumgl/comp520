/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

//ERROR: LHS(Primitive type) = RHS(Custom type) fails

package main

func main() {
	type dog int;
	var chihuahua dog;
	
	var cat int;
	cat = chihuahua; //ERROR
}
