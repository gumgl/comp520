/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

var a int;
var b int;
var c int;
var d int;


func main() {
	b = 1;
	c = 1;

	println(b);
	println(c);

	a = 100;

	for a != 0 {
		println(b + c);
		d = b;
		b = c;
		c = c + d;
		a = a - 1;
	}
}
