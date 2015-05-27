/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

// Assumes nonnegative n
func fact(n int) int {
   if n == 0 {
      return 1
   } else {
      return n * fact(n - 1)
   }
}

func main() {
   
}
