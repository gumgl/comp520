/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

public class GoLiteTypeException extends RuntimeException {
	private static final long serialVersionUID = -563134195929039034L;

	public GoLiteTypeException() {
	}

	public GoLiteTypeException(String message) {
		super(message);
	}

	public GoLiteTypeException(Throwable cause) {
		super(cause);
	}

	public GoLiteTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public GoLiteTypeException(String message, Throwable cause,
			boolean enableSuppression, boolean writeableStackTrace) {
		super(message, cause, enableSuppression, writeableStackTrace);
	}

}
