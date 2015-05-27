/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func test() bool {
	x := 3;
	y := 3.0;
	if x < 3 {
		if y == 2.2 {
			return true;
		}
	} else {
		return false;
	}
}

func main() {
	test();
}
