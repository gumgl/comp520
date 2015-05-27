/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

// invalid short declaration

package main

func main() {
var (
a, b int
x := 0
)
if false {
println(a, b, x)
}
}