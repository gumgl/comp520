/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func plusminus(a,b int, c bool) int {
	var result int
	if c {
	result=a+b
	} else {
	result=a-b
	}

	return result
}

func main(){
	printThis:=plusminus(5,10,true)
	println(printThis)
	printThis=plusminus(5,10,false)
	println(printThis)
}
