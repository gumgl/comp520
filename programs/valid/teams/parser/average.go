/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
	var x []float64
	x = append(x, 1)
	x = append(x, 2)
	x = append(x, 3)
	println(average(x))
}

func average(x []float64) float64 {
	var value float64

	total := 0.0
	for i := 0; i < len(x); i++ {
		value = x[i]
		total += value
	}

	return total / float64(len(x))
}
