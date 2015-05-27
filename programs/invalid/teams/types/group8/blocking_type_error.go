/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	type digit int; 
	{
		var digit_one digit = digit(1);
		{
			type digit float64;
			var digit_two digit = digit(1.0);
			print((digit_one == digit_two))
		}
	}
}
