package main;

var (
	square = 1764
)


func main() {
	var y int,z float = 1,2
	x := float64(square/2)
	for i:= 0; i < 20; i++ {
		x = x - (x*x - float64(square))/(2*x);

	}
	println("The Square root of ", square, " is roughly ", x);

}