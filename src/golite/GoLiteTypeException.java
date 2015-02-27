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
