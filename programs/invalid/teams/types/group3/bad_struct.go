package main

type person struct {
	name string
	age int
}

func main() {
	// This doesn't type check because the struct person doesn't have a lastname field
	var bob person
	println(bob.lastname)
}
