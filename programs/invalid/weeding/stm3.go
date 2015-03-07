package main

func main () {
	
	var a int = 5
	
	//didn't include infinite loop
	
	for a>0 {
		println(a)
		a--
	}
	
	//error: expr is not boolean
	for a+=a {
		println(a)
	}
	
	//error: expr of println is not wel typed
	println("a"+'a')

}
