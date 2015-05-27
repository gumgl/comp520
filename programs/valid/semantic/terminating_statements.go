/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

// See http://golang.org/ref/spec#Terminating_statements

func term_return_stmt() int {
	return 1
}

func term_block() int {
	{
		return 1
	}
}

func term_if() int {
	if false {
		return 0
	} else {
		return 1
	}
}

func term_endless_for() int {
	for i := 0;; i++ {
		if i > 997 && i % 997 == 0 {
			return i
		}
	}
}

func term_switch() int {
	var c = 'a'
	switch c {
	case 'b':
		return int('c')
	case 'c':
		fallthrough
	default:
		fallthrough
	case 'z':
		return -1
	}
}

func main() {
	println("All cases are good!")
}
