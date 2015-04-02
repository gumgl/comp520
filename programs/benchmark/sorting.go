package main

var seed int = 123456789;

func rand() int {
	//seed = (a * seed + c) % m;
	seed = (16807 * seed + 12345) % 1024;
	return seed;
}

func bubblesort(a []int, length int) {
    for itemCount := length - 1; ; itemCount-- {
        hasChanged := false
        for index := 0; index < itemCount; index++ {
            if a[index] > a[index+1] {
                a[index], a[index+1] = a[index+1], a[index]
                hasChanged = true
            }
        }
        if hasChanged == false {
            break
        }
    }
}

func main() {
	var list []int
	var n = 125000
	println("Generating random list of size", n, "...")
	for i := 0; i < n; i++ {
		list = append(list, rand())
	}
	println("Sorting...");
	bubblesort(list, n)
	println("Done!")
}
