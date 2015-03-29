package main

func test1() {
	println("TEST 1: both fall through")
	for x := 1; x <= 3; x++ {
		println("Entering", x)
		switch x {
		case 1:
			println(1)
			fallthrough
		default:
			println(2)
			fallthrough
		case 3:
			println(3)
		}
	}
}

func test2() {
	println("TEST 2: first falls through")
	for x := 1; x <= 3; x++ {
		println("Entering", x)
		switch x {
		case 1:
			println(1)
			fallthrough
		default:
			println(2)
		case 3:
			println(3)
		}
	}
}

func test3() {
	println("TEST 3: second falls through")
	for x := 1; x <= 3; x++ {
		println("Entering", x)
		switch x {
		case 1:
			println(1)
		default:
			println(2)
			fallthrough
		case 3:
			println(3)
		}
	}
}

func main() {
	test1()
	test2()
	test3()
}
