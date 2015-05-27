/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

var seed int = 123456789;

func rand() int {
	//seed = (a * seed + c) % m;
	seed = (69069 * seed + 1) % 4294967296
	return seed
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
