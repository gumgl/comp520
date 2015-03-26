package main

func get_array() [5]int {
	var ints [5]int
	return ints
}

func main() {
	get_array()[3] += 3
}
