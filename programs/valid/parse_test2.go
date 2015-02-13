/* Package declaration, golite 2.2 */
package parse_test2

func main() {
  n := 5
  funky(n)
  
  number(35)
}


/* Append, golite 2.9.7 */ 
/* Switch statements, golite 2.8.10 */
func funky(n int){
	 
  s0 := []int{0,1%5}
  s1 := append(s0, 2) 
  s2 := append(s1, 3+5) 

	switch n {
	default: println(s2[1])
	case 1,2,3,4: println(s0[1])
	case 5,6: println(s2[3]) 
	}
	return 
}

func number(a int) int {

/* If statements, golite 2.8.9  */
/* Print and println statements, golite 2.8.7 */

if a<0 {
	println(a,"is negative.")
}


if a%2 == 0 {
  println(a,"is even.")
} else {
	println(a,"is odd.") 
}


/*Short declaration statements, golite 2.8.5 */
b:=50
c:=b-a


if c>0 {
	println(a,"<50.")
} else if c<0 {
  println(a,">50.")
} else {
  println(a,"=50.")
}

return a
}
