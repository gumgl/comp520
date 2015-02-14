import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import golite.node.*;
import golite.analysis.ReversedDepthFirstAdapter;


public class TokenMapper extends ReversedDepthFirstAdapter {
	private Map<Node, Token> map = new HashMap<Node, Token>();
	    
	private Token lastToken;
	
	
	@Override
	public void defaultCase(Node node) {
	    lastToken = (Token) node;
	}
	
	@Override
	public void defaultOut(Node node) {
	    if(!(node instanceof Token))
	        map.put(node, lastToken);
	}
	
	public Map<Node, Token> getMap() {
	    return Collections.unmodifiableMap(map);
	}

}
