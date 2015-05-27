/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*2. Statements */
func main(){

	/*2.8 Op-assignmet*/
	var a float64
	var b int
	a = 1.0
	b = 2
	//error: invalid operation: a *= b (mismatched types float64 and int)
	a*=b
	print(a,b)
	
}
