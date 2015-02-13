/* Package declaration, golite 2.2 */
package parser_test_invalid1

func main() {
  
  funky2()
  
  /*error: cannot use "lalala" (type string) as type float64 in return argument */
  
  a_float := func() float64 {return "lalala"} 
  a_float()  
  
  /*error:  trailing comma not allowed, golite 2.9.6*/
  println("jibber jabber,"trailing coma",)
  
}


func funky2(){
	
	n := 5
	s0 := []int{0,1,3,5}
	s1 := append (s0,7,9)
	s2 := append (s0,11) 
	
	/* Switch, golite 2.8.10 */	
	switch i:=n-1;i {
	default: println(s2)
	case 1,2.0: println(s0[1])
	case 3,4: println(s1[1])
	
	/*error:  multiple defaults in switch */
	/*need a weeding phase to make sure that there is only one default case.*/
	default: println(s0[1])
	}

    /* Return statements, golite 2.8.7 */
	return 
}
