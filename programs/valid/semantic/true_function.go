package main

func true() bool {
    return !false
}

func main() {
	println("It is weird that this is possible:", true())
}
