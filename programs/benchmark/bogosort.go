package main

//import "fmt"

var seed int = 2834324

func rand(max int) int {
	//seed = (a * seed + c) % m;
	seed = (69069 * seed + 1) % 4294967296
	if max != 0 {
		return seed % max
	} else {
		return seed
	}
}

func sorted(elements []int, size int) bool {
	for i := 0; i < size-1; i++ {
		x := elements[i]
		y := elements[i+1]
		if x > y {
			return false
		}
	}
	return true
}

func shuffle(elements []int, size int) []int {
	for i := 0; i < size-1; i++ {
		j := rand(size)
		tmp := elements[i]
		elements[i] = elements[j]
		elements[j] = tmp
	}
	return elements
}

func bogosort(elements []int, size int) int {
	for i := 0; ; i++ {
		if sorted(elements, size) {
			return i
		} else {
			shuffle(elements, size)
		}
	}
}

func main() {
	var list []int
	var n = 12
	println("Generating random list of size", n, "...")
	for i := 0; i < n; i++ {
		list = append(list, rand(0))
	}
	println("Sorting...")
	//fmt.Println("%v", list)
	var its = bogosort(list, n)
	println("Sorted in", its, "iterations.")
}
