package golite;
/* Insert semicolons without removing syntactically insignificant tokens
 * from the stream, yielding a syntactically valid program that could be
 * parsed without the customized lexer.
 */

import golite.lexer.IPushbackReader;
import golite.node.TSemicolon;
import golite.node.Token;

import java.io.PushbackReader;
import java.util.LinkedList;


public class ConservingGoLexer extends GoLexer {
	LinkedList<Token> pendingTokens;

	public ConservingGoLexer(PushbackReader in) {
		super(in);
		pendingTokens = new LinkedList<Token>();
	}

	public ConservingGoLexer(IPushbackReader in) {
		super(in);
		pendingTokens = new LinkedList<Token>();
	}

	protected void filter() {
		// If required, insert a semicolon before the token
		if (requiresSemicolon()) {

			lastSignificantToken = newSemicolonAfter(lastSignificantToken);

			if (isWhitespaceOrComment(pendingTokens.peekLast())) {
				LinkedList<Token> pendingIgnored = new LinkedList<Token>();

				do {
					pendingIgnored.addFirst(pendingTokens.removeLast());
				} while (isWhitespaceOrComment(pendingTokens.peekLast()));

				pendingTokens.add(lastSignificantToken);
				for (Token ignored : pendingIgnored) {
					pendingTokens.add(ignored);
				}
			} else {
				pendingTokens.add(lastSignificantToken);
			}
		} else if (!isWhitespaceOrComment(token)) {
			lastSignificantToken = token;
		}

		// If there are tokens pending, use the first one now
		if (!pendingTokens.isEmpty()) {
			pendingTokens.add(token);
			token = pendingTokens.remove();
		}
	}
}
