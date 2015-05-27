/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

/* Package declaration, golite 2.2 */
package parse_test4

func main() {

println("6!=720 and we have...",fact(6))


/*golite 2.9.1-2.9.5*/
/* GoLite supports all the Go binary operators */
//BasicLit= int_lit | float_lit | imaginary_lit | rune_lit  

a:=(1+((71%(-13+6))*7))
b:=1+71%(-13+6)*7
c:=(1+2)/4
d:=1+2/4

//+= operator

s := "jibber"
s += "jabber"
s += " 1 "
s +=(string(c) + "jabber") //string(c) does nothing?
println("the following should be 8 8 0 1 and we have...",a,b,c,d,s)
 
//Logical operators
//http://golang.org/ref/spec#Operators to be added in the morning


}

/* Function calls, golite 2.9.6*/
func fact(n int) int {
    if n == 0 {
        return 1
    }
    return n * fact(n-1)
}
