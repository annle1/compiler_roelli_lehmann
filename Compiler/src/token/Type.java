package token;

public enum Type implements IToken{
	BOOL("(TYPE, BOOL)"),
	INT64("(TYPE, INT64)");

	private String value;

	Type(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
