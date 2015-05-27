/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

type account struct {
	balance int
}

type person struct {
	name string
	account account
}

func main() {

	var chequing account
	var charlie person
	
	chequing.balance = 200
	
	charlie.name = "Charlie"
	charlie.account = chequing
	
	charlie.account = withdraw(150, chequing)
	charlie.account = deposit(30, chequing)
	charlie.account = deposit(50, chequing)
	
	switch {
	case charlie.account.balance < 0: println(charlie.name + ", No money")
	case charlie.account.balance < 200: println(charlie.name + ", Getting there")
	case charlie.account.balance < 500: println(charlie.name + ", Oulala")
	default: println("Marry me?")
	}
}

func withdraw(amount int, a account) account{
	if a.balance >= amount {
		a.balance -= amount
	} else {
		println("Cannot withdraw the amount.")
	}	
	return a;
}

func deposit(amount int, a account) account {
	a.balance += amount	
	return a;
}
