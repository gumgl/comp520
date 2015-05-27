/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// mismatched types rune and string

package main


func main() {
	x,y := 'x',"x"
	print(x%y)
}

