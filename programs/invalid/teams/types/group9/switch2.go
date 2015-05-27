/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

var y int = 1
func switch_stmts() {
	switch y+"str" {
	case 0: "zero"
	case 1, 3: "odd" 
	}
}
