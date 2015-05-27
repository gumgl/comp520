/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

//ERROR: operand should be of type in/rune
package main;

func main () {
	var float_var float64 = 1.0;

	//ERROR: will fail for every one of these
	//for the following operators, only the operands should only be int & rune
	float_var |= 1.0;
	float_var &= 1.0;
	float_var <<= 1.0;
	float_var >>= 1.0;
	float_var &^= 1.0;
	float_var ^= 1.0;

}
