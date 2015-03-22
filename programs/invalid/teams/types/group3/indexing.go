package main

func main() {
	var a [5]int
	// This does not type check, the array index must be an int or a slice
	println(a['0'])
}
