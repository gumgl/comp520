/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

//error: adder shouldnt have return argument
func adder (a int, b int) {
    var c = a+b
    print(c)
    return c
}

func main(){
	adder(2,3)
}
