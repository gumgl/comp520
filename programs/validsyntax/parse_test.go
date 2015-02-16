/* This program doesn't do anything useful, but it covers most of the
 * constructs we need to implement for Golite */

// I've covered up to section 2.8.2 so far

/* Package declaration, golite 2.2 */
package parse_test

/* Top-level declarations, golite 2.3 */

/* Type declarations, golite 2.5
 * Examples from the Go spec: http://golang.org/ref/spec#Type_declarations */
type IntArray [16]int

type (
    Point struct { x, y float64; } // Some comment here; a semicolon should be inserted anyway
    Polar Point
)

/* Function declarations, golite 2.6 */
func main() {
    /* Literals, golite 1.5; variable declarations, golite 2.4 */
    var x int = 255
    var (
        y, z = 0377, 0xff
        a, b, c float64 = 12., .12, 0.12
    )
    var esc_rune = '\n'
    var interp_string, raw_string = "hello\n\a\b\f\r\t\v\\'", `hello
`
    // Slice types, 2.7.2; Array types, 2.7.3
    var s [2]string; var _p []Polar

    s[1] = interp_string
    s[2] = raw_string
    _p.x = a; _p.y = b
}

func f(a, b int, c rune) {
    // empty statement, 2.8.1
    ;
}
