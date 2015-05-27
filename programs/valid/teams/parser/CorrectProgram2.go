/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	var a int = 2
	var b int = 5
	var c bool = false
	var d bool = false

	for a < b {
		if c {
			a += 1
		} else if d {
			print("d was true")
		} else {
			b -= 1
		}
	}
	print(b)
}

