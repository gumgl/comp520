package main

// Using Go, this runs in about 20 seconds on my laptop

func ackermann(m, n int) int {
	if m > 0 && n == 0 {
		m--
		n = 1
	}

	if m == 0 {
		return n + 1
	}

	return ackermann(m-1, ackermann(m, n-1))
}

func main() {
	println(`Where A(m, n) =
    n + 1             if m = 0
    A(m-1, 1)         if m > 0 and n = 0
    A(m-1, A(m, n-1)) otherwise`)

	println("A(3, 12) =", ackermann(3, 12))
}
