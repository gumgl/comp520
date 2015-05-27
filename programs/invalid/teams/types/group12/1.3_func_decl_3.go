/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// f should have a return statement on every execution path
package main

func f (x int) int {
	if x>0 {
		print(x)
	} else {
		return x
	}
}

func main() {

	print(f(2))

}
