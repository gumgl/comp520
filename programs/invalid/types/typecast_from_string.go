/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main(){
	var a float64
	var d string
	a = 2.0
	b:=int(a)
	c:=float64(a)
	
	d="aa"
	//d:=bool(a)
	e:=rune(a)
	
	println(a,b,c,d,e)
	
	z:=rune(d)
	print(z)
}
