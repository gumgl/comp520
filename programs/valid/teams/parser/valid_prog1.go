/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

var (
	rollingAvr float64 = 0
	windowSize = 10;
	n int = 0
	maxN int = 100
	ratio float64 = 0.9
	z string = "here"
)

func main() {
	for n = 1 ; n < windowSize ;  n++{ // initialize rollingAvr
		rollingAvr += float64(n);
	}
	rollingAvr /= float64(windowSize);
	
	print("Rolling Average initialized to: ")
	println(rollingAvr)
	
	for ; n < maxN; n++ {
		rollingAvr *= float64(windowSize) * ratio;
		rollingAvr += float64(n);
		rollingAvr /= float64(windowSize)
	}
	
	print("Final rolling average is ")
	println(rollingAvr)
}
