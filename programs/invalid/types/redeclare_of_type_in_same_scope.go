/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*Declarations 1.2& 1.3*/

func main(){
	
	type num int
	var a num = 2
	print(a)
	//type redeclaration in inner scope is ok
	{
		type num bool
		var b num = true
		println(b)
	}
	
	//error: redeclaration in the same scope
	type num bool
	var b num = true
	print(b)
}
