/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

/* Package declaration, golite 2.2 */
package main

func main() {

	/*2.13 Switch statement*/
	x := 5
    	print("x should be ",x,". It is ")
    	switch x {
    		case 5:
        		println("five")
    		case 10:
        		println("then")
	    	default: 
	    		println("nah")
    	}
    	switch {
    		case x<5:
    			println("x<5")
    		case x<10 && x>=5:
    			println("5<x<10")
    		default:
    			println("x>=10")
    	}
    	
    	/*3.6 Indexing*/
	/*3.7 Field Selection */
	/*3.8 Append*/
	/*3.9 Type cast*/
	
	
	
}
