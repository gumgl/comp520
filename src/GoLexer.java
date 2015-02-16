/* Automatic semicolon insertion, based on Vincent Foley-Bourgon's
 * example code */

import java.io.PushbackReader;

import golite.lexer.IPushbackReader;
import golite.lexer.Lexer;
import golite.node.*;

public class GoLexer extends Lexer {
	protected Token lastSignificantToken = null;

	public GoLexer(PushbackReader in) {
		super(in);
	}

	public GoLexer(IPushbackReader in) {
		super(in);
	}

	protected void filter() {
		// If required, insert a semicolon before the token
		if (requiresSemicolon()) {
			lastSignificantToken = token = newSemicolonAfter(lastSignificantToken);
		} else if (!isWhitespaceOrComment(token)) {
			lastSignificantToken = token;
		}
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
	protected boolean requiresSemicolon() {
		if (!(token instanceof TEol)) return false;

		return (lastSignificantToken instanceof TId
			// literals
			|| lastSignificantToken instanceof TLitInt
			|| lastSignificantToken instanceof TLitFloat
			|| lastSignificantToken instanceof TLitHex
			|| lastSignificantToken instanceof TLitOctal
			|| lastSignificantToken instanceof TLitRaw
			|| lastSignificantToken instanceof TLitRune
			|| lastSignificantToken instanceof TLitInterpreted
			// keywords
			|| lastSignificantToken instanceof TBreak
			|| lastSignificantToken instanceof TContinue
			|| lastSignificantToken instanceof TReturn
			// operators
			|| lastSignificantToken instanceof TPlusPlus
			|| lastSignificantToken instanceof TMinusMinus
			// brackets
			|| lastSignificantToken instanceof TParenR
			|| lastSignificantToken instanceof TSquareR
			|| lastSignificantToken instanceof TBraceR
			// builtin type names
			|| lastSignificantToken instanceof TBool
			|| lastSignificantToken instanceof TInt
			|| lastSignificantToken instanceof TFloat64
			|| lastSignificantToken instanceof TRune
			|| lastSignificantToken instanceof TString);
	}

	protected static boolean isWhitespaceOrComment(Token tok) {
		return tok instanceof TWhitespace || tok instanceof TComment;
	}

	protected TSemicolon newSemicolonAfter(Token tok) {
		TSemicolon semi = new TSemicolon();
		semi.setLine(tok.getLine());
		semi.setPos(tok.getPos());
		return semi;
	}
}
