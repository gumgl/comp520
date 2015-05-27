/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main 

func random() int {
	//Suppose this is a random integer
	return 0
}

func main() {
	// The clause doesn't type check because the initial statement doesn't type check
	switch x := 0.0 + random(); x {
		// Doesn't matter
	}
}
