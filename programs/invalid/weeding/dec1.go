package main

/*Variable Declarations 1.1*/

func main(){
	var a int
	var b float64
	var c bool
	var d rune
	var e str
	a = 2
	b = 1.0
	c = true
	d = 'h'
	e = "huh"
	
	var f int = a * a
	var g = b
	println(a," ",b," ",c," ",d," ",e," ",f)
	{
	var b bool
	b = true
	println("redeclare of b in inner scope:", b)
	}
	//error: redeclare c
	var c int
	c = 1
	println(c)
}