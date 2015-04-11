package main

// This works if a new scope is opened for the switch statement as a whole
// and for each individual case
func main() {
	x := 1
	y := 2

	switch x := true; x {
	case true:
		y := "true"
		println("got", y)
	case false:
		y := "nothing worthwhile"
		println("got", y)
	default:
		y := "this is actually impossible"
		println(y)
	}
	println("meanwhile, x =", x, "and y =", y)
}
