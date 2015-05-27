/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main 

func random() int {
	//Suppose this is a random integer
	return 0
}

func randomBool() bool {
	//Suppose this is randomly true or false
	return false
}

func main() {
	// The switch block doesn't type check because the second case doesn't have a boolean type
	switch x := random(); {
		case true, randomBool():
			println("TRUTH!")
		case 0:
			println("ZEROOO!")
		default:
			println("I don't know what's up...")
	}
}
