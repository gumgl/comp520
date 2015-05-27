/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

type salary float64

type employee struct {
	id int
	name string
	sex rune
	wage salary
}

func main() {
	println(3, 3.141592, "hello", 'c', true)

	var e employee
	println(e.id, e.name, e.sex, e.wage)
}
