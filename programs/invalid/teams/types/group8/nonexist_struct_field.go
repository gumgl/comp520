/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

type digit int;
type num float64;
type Circle struct {
	radius float64;
	diameter float64;
}

func main() {
	
	var circle_one Circle;
	circle_one.radius = 5.0;
	circle_one.diameter = 10.0;
	circle_one.area = 2*3.14*25.0;
	print(circle_one.area);
	
}
