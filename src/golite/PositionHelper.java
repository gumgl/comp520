package golite;

import golite.node.Node;
import golite.node.Token;

import java.util.Map;

public class PositionHelper {

	private Map<Node, Token> tokenMap;

	public PositionHelper(Node ast) {
		TokenMapper mapper = new TokenMapper();
		ast.apply(mapper);
		tokenMap = mapper.getMap();
	}

	public Token getTok(Node n) {
		if (n instanceof Token) return (Token) n;

		Token t = tokenMap.get(n);

		if (t == null) {
			throw new IllegalArgumentException("Node was not found in AST");
		}

		return t;
	}

	public int getLine(Node n) {
		return getTok(n).getLine();
	}

	public int getPos(Node n) {
		return getTok(n).getPos();
	}

	public String lineAndPos(Node n) {
		Token t = getTok(n);
		return "[" + t.getLine() + "," + t.getPos() + "]";
	}

}
