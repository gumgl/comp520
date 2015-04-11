package main

func main() {
	a := 1
	b := a
	println("ints:", a == b)
	println("inequality:", a != b)

	c := "xyz"
	d := "xy"
	println("strings:", c == d+"z")

	var e, f [1]int;
	println("arrays:", e == f)

	var g, h struct { x int; }
	println("structs:", g == h)
    println("struct inequality:", g != h)
}
