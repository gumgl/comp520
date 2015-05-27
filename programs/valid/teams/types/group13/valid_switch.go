/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/*
 * Basic switch statement example, type of variable being switch must
 * be same type of each case
 */
func main() {
    var x float64 = 5.0;
    switch a := 1; x {
        case 5.0: print(5);
        case 10.0 : print(10);
        case 1.0,50.0: print(2);
        default: print("default");
    }
}
