package main

type container struct{ i int; }

func main() {
	var a [1]container
	a[0].i = 23

	var b = a
	b[0].i = 77
	
	println("Expected 23, got", a[0].i)
	println("Expected 77, got", b[0].i)
}
