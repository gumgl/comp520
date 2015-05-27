/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

/* This program is tries to select an undeclared field from a struct */

package main

func main() {
	var x struct
	{
		y int
	}
	x.z = 4
}
