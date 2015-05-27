/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	if x := 0; x == 0 {
		x++
	}

	if x := 0; x == 0 {
		x++
	} else if y := 1; y < 0 {
		y++
	}

	switch x := 0; x {
	case 0: x++
	default: x = 0
	}

	for x := 0; x < 10; x++ {
		x++
	}
}
