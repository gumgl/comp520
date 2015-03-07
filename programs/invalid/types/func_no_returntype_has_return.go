package main

func main(){

	adder(2,3)
}

//error: adder shouldnt have return argument
func adder (a int, b int) {
	var c = a+b
	print(c)
	return c
}
