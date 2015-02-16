package parse_test5

//optional semicolons
//multidim array example
//from http://www.tutorialspoint.com/go/go_multi_dimensional_arrays.htm

func main() {
// http://www.cs.sfu.ca/CourseCentral/354/zaiane/material/notes/Chapter2/node5.html
/* an array with 5 rows and 2 columns*/
var a [5][2]int;
var b [3][5]int;
var i, j int
;

// In GoLite we do array initialization by index
a[0][0] = 0
a[0][1] = 0
a[1][0] = 1
a[1][1] = 2
a[2][0] = 2
a[2][1] = 4
a[3][0] = 3
a[3][1] = 6
a[4][0] = 4
a[4][1] = 8

/* output each array element's value */
for  i = 0; i < 5; i++ {
for j = 0; j < 2; j++ {
/* legal backslash sequence */
println( i, j, a[i][j],"hello\"there")
  };
}

println("should be \r0\r1\r2\r3 and we have")
for i := 0; i < 3; i++ {
  h := b[i]
  println(h);}
  println(len(b));
};
