/* Package declaration, golite 2.2 */
package main

func main() {

	/*Variables Declarations 1.1 */
	var num1 int
	num1 = 12
	var num2 int = num1 * 1.0 + 2.0
	
	/*Block*/
	{var num3 = num1 * num1
	println("In the block. num3 should be 144:", num3)
	}
	
	var num3 = num1 * 3.0
	println( "Three numbers should be 12 14 36 and they are:",num1,num2,num3 )
	
	/*Types Declarations 1.2 */
	type a int 
	var x a = 2
	println( "x is of type a and it should be 2:",x )
	
	/*redeclare type in inner scope*/	
	{type a bool
	var x a = false 
	println(x) 
	}
	
	var res int = regularPower(2,3)
	println("2^3 is:",res)
	
	
}

	
/*Functions Declarations 1.3 */
func regularPower(a int, b int) int {
	var res int
	res = 1
	for b>0 {
		res *= a
		b --
   }
   return res 
}
