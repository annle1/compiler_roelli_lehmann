package exception;

public class GrammarError extends Exception{
	private static final long serialVersionUID = 1L;
	
	public GrammarError () {
		
	}
	
	public GrammarError(String errorMessage) {
		super(errorMessage);
	}
	
	public GrammarError(String message, Throwable cause) {
		super(message, cause);
	}

	public GrammarError(Throwable cause) {
		super(cause);
	}
}
