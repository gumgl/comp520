/* Automatic semicolon insertion, based on Vincent Foley-Bourgon's
 * example code */
import java.io.PushbackReader;
import java.util.LinkedList;

import golite.lexer.IPushbackReader;
import golite.lexer.Lexer;
import golite.node.*;

public class GoLexer extends Lexer {
	private Token last_token = null;
	LinkedList<Token> pending_tokens;

	public GoLexer(PushbackReader in) {
		super(in);
		pending_tokens = new LinkedList<Token>();
	}

	public GoLexer(IPushbackReader in) {
		super(in);
		pending_tokens = new LinkedList<Token>();
	}

	/*
	 * Insert semicolons based on rule 1 in the Go spec
	 * (http://golang.org/ref/spec#Semicolons):
	 *
	 * When the input is broken into tokens, a semicolon is automatically
	 * inserted into the token stream at the end of a non-blank line if the
	 * line's final token is:
	 *
	 *  - an identifier
	 *  - an integer, floating-point, rune, or string literal
	 *  - one of the keywords break, continue, or return
	 *  - one of the operators and delimiters ++, --, ), ], or }
	 *
	 *  (The fallthrough and imaginary token types are not part of GoLite, and
	 *  so are omitted.)
	 */
	private boolean requiresSemicolon() {
		if (!(token instanceof TEol)) return false;

		return (last_token instanceof TId
			// literals
			|| last_token instanceof TLitInt
			|| last_token instanceof TLitFloat
			|| last_token instanceof TLitHex
			|| last_token instanceof TLitOctal
			|| last_token instanceof TLitRaw
			|| last_token instanceof TLitRune
			|| last_token instanceof TLitInterpreted
			// keywords
			|| last_token instanceof TBreak
			|| last_token instanceof TContinue
			|| last_token instanceof TReturn
			// operators
			|| last_token instanceof TPlusPlus
			|| last_token instanceof TMinusMinus
			// brackets
			|| last_token instanceof TParenR
			|| last_token instanceof TSquareR
			|| last_token instanceof TBraceR
			// builtin type names
			|| last_token instanceof TBool
			|| last_token instanceof TInt
			|| last_token instanceof TFloat64
			|| last_token instanceof TRune
			|| last_token instanceof TString);
	}

	protected void filter() {
		// If required, insert a semicolon before the token
		if (requiresSemicolon()) {
			last_token = new TSemicolon();
			pending_tokens.add(last_token);
		} else {
			last_token = token;
		}

		// If there are tokens pending, use the first one now
		if (!pending_tokens.isEmpty()) {
			pending_tokens.add(token);
			token = pending_tokens.remove();
		}
	}
}
