/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main(){
var x bool=true
var y string
y=string(x)
}

//This program will fail the type check because you can't cast from a bool to a string. We check for this in castK under typeFactor in GoType.c
