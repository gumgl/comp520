package parse_test5

//optional semicolons
//multidim array example
//from http://www.tutorialspoint.com/go/go_multi_dimensional_arrays.htm

func main() {
http://www.cs.sfu.ca/CourseCentral/354/zaiane/material/notes/Chapter2/node5.html
/* an array with 5 rows and 2 columns*/
var a = [5][2]int{ {0,0}, {1,2}, {2,4}, {3,6},{4,8}};
var b [3][5]int;
var i, j int
;

/* output each array element's value */
for  i = 0; i < 5; i++ {
for j = 0; j < 2; j++ {
println( i, j, a[i][j] )
  };
}

println("should be \r0\r1\r2\r3 and we have")
for h := range b {
  println(h);}
  println(len(b));
};
