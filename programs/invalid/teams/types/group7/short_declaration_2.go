/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

//ERROR: no new variable on LHS of short declaration

package main;

func main(){
	var a,b,c int;
	a,b,c := 1,2,3; //ERROR: fails because there is no new variables on the LHS
}
