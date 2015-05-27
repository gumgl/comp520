/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

/* Valid rune assignment example, testing with all escaped characters */
func main() {
	var r1, r2, r3, r4, r5, r6, r7, r8, r9 rune;
	r1 = '\a'
	r2 = '\b'
	r3 = '\f'
	r4 = '\n'
	r5 = '\r'
	r6 = '\t'
	r7 = '\v'
	r8 = '\\'
	r9 = '\''
	print(r1, r2, r3, r4, r5, r6, r7, r8, r9)
}
