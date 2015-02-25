Taken from https://groups.google.com/d/msg/sablecc/W1ht6ARgQgs/u1Yf-ImZdPcJ

## Setup
Declare the TokenMapper and apply it to the AST:
```java
main() {
	Start ast = parser.parse();
	TokenMapper tm = new TokenMapper();
	ast.apply(tm);
	tokenMap = tm.getMap();
}
public static Token getFirstToken(Node node) {
	if(node instanceof Token)
		return (Token) node;
	else
		return (Token) tokenMap.get(node);
}
```

## Use
Starting from an AST node/token, get its line number and position: 
```java
void error(Node node) {
	Token token = getFirstToken(node);
	System.out.println("Error at ["+token.getLine()+","+token.getPos()+"]");
}
```