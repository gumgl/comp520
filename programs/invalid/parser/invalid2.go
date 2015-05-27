/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

/* Package declaration, golite 2.2 */
package parser_invalid2

/* These cases are handled as type errors */

func main(){

/*Type casts, golite 2.9.8*/
/* example from the given GoLite Syntax Specification*/

type num int
var x int = 3
var y num = num(x)
var z float64 = float64(x)

/*error: cannot convert x (type int) to type bool */
var know bool = bool(x)

/*error: cannot convert x (type int) to type string */
/*go compiler accepts this tho.*/
var know2 string = string(x)


}
