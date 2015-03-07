package main

func main() {
// struct type

	// empty struct is ok
	type nada struct {
	}
	
	
	type aaa struct {
		a int
		b bool
		c string
	}

	sample := aaa{2,true,"2"}
	println(sample.d)



}
