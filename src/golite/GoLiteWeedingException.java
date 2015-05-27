/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

public class GoLiteWeedingException extends RuntimeException {
	private static final long serialVersionUID = 868259916037491128L;

	public GoLiteWeedingException() {
	}

	public GoLiteWeedingException(String message) {
		super(message);
	}

	public GoLiteWeedingException(Throwable cause) {
		super(cause);
	}

	public GoLiteWeedingException(String message, Throwable cause) {
		super(message, cause);
	}

	public GoLiteWeedingException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
