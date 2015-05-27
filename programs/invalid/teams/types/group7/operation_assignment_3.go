/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

//ERROR: operand are not of type
package main;

func main () {
	var int_var int = 1;
	var rune_var rune = 'a';

	//ERROR: operands are of different types
	int_var -=  rune_var;

}
