package parser_invalid3

func main(){
 
//illegal backslash sequence
//error:unknown escape sequence: '
println("hello\'there")

a:= 5
b:= 10

//no multiple returns accepted
s,t := compare(a,b)
println(s,t)

}

func compare(y int,z int)(int,int){
if x := 3; x > y {
	return x,y;
} else if x > z {
	return x,z
} else {
	return y,z
}
}
