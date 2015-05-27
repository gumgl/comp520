/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package parser_invalid4

//func goto package if else return
//case defer go map struct

func main() {
  x:=28

//error:even(x) used as value
//y:=even(x) 

  even(x)
  print

//error:too many arguments to return
  return x
}





func even(x int) {
if x%2==0 {goto skip}
else {goto msg}

msg:
  print("odd number")
skip:
  print("even number")
}
