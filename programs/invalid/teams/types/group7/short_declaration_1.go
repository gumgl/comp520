/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

//ERROR: LHS type does NOT match RHS type

package main;

func main(){
	var a,b,c int;
	a,b,c,d := 1,2,3,4;

	d,e := 5.0 ,6; //ERROR:fails because expresion for varibale "d" is type float46 instead of int
}
