package parser_test_invalid5


//valid in Go, not in Golite. 
//Golite doesn't support pointer

func main(){

//error:println(2) used as value
_=println("a")

a:=1
pointer(&a)
println("a:",a)

}


func pointer(aptr *int) { 
*aptr = 0 
} 
