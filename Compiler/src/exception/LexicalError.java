package exception;

public class LexicalError extends Exception {
	private static final long serialVersionUID = 1L;

	public LexicalError() {
		super();
	}

	public LexicalError(String message) {
		super(message);
	}

	public LexicalError(String message, Throwable cause) {
		super(message, cause);
	}

	public LexicalError(Throwable cause) {
		super(cause);
	}
}
