/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// cannot assign unnamed type (int) to named type (integer)
// note: this typechecks in Go

package main

func main() {
    type integer int
    var x, y bool
    var z integer
    x, y, z = true, false, 5
    print(x, y, z)
}
