package main

type person struct {
	name string
	age int
}

func main() {
	// This doesn't type check because a struct cannot be typecast
	var bob person
	println(int(bob))
}
