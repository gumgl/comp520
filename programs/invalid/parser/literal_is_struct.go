package main 

//golite doesn't support struct literals

func main() {

	type flat struct {
		x, y int
	}

	var a = flat{1, 1}  

}
