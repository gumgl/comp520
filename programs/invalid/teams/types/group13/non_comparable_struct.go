package main

/* Note: according to the Go spec, the only problem here is with
 * the s field; slice values are not comparable, and structs are
 * only comparable if all their fields are. */

func main() {
	type st struct {
		b bool;
		f float64;
		i int;
		s []int;
	}

	var st1, st2 st;

	print(st1 == st2)
}
