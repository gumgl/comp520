package main

/*Declarations 1.2& 1.3*/

func main(){
	
	type num int
	var a num = 2
	print(a)
	//type redecleration in inner scope is ok
	{
		type num bool
		var b num = true
		println(b)
	}
	
	//error: redeclaration in the same scope
	type num bool
	var b num = true
	
	adder(2,3)
	f(2)
}

func adder (a int, b int) int {
	return a+b
}
//error: redeclaration in the same scope
func adder (a int, b int) int {
	return a+b+b
}

func f(f int){
	// error: redeclare f
	var f bool
	f = true;
}
