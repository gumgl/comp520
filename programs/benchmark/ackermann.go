package main

// Using the Go compiler, this runs in about 50-90
// seconds depending on the machine

type stack struct{
	store []int;
	index, allocated int;
}

func push(s stack, value int) stack {
	s.index++

	if s.allocated >= s.index {
		s.store[s.index-1] = value
	} else {
		s.allocated++
		var store = s.store
		s.store = append(store, value)
	}

	return s
}

func ackermann(m, n int) int {
    var s stack

    s = push(s, m)
    s = push(s, n)

	// Continue applying the function until the stack
	// is reduced to the return value
    for s.index > 1 {
		// Pop the top values from the stack
		var m, n = s.store[s.index-2], s.store[s.index-1]
		s.index -= 2

		// ackermann(m, 0) -> ackermann(m-1, 1)
		if m > 0 && n == 0 {
			m--
			n = 1
		}

		if m == 0 {
			// ackermann(0, n) -> n + 1
			s = push(s, n + 1)
		} else {
			// ackermann(m-1, ackermann(m, n-1))
			s = push(s, m-1)
			s = push(s, m)
			s = push(s, n-1)
		}
	}

	return s.store[0]
}

func main() {
	println(`Where A(m, n) =
    n + 1             if m = 0
    A(m-1, 1)         if m > 0 and n = 0
    A(m-1, A(m, n-1)) otherwise`)

	println("A(3, 12) =", ackermann(3, 12))
}
