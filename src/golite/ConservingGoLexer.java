/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;
/* Insert semicolons without removing syntactically insignificant tokens
 * from the stream, yielding a syntactically valid program that could be
 * parsed without the customized lexer.
 */

import golite.lexer.IPushbackReader;
import golite.node.Token;

import java.io.PushbackReader;
import java.util.LinkedList;


public class ConservingGoLexer extends GoLexer {
	LinkedList<Token> pendingTokens = new LinkedList<Token>();

	public ConservingGoLexer(PushbackReader in) {
		super(in);
	}

	public ConservingGoLexer(IPushbackReader in) {
		super(in);
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
