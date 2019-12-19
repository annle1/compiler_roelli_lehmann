package token;

public enum Mechmode implements IToken{
	COPY("(MECHMODE, COPY)"),
	REF("(MECHMODE, REF)");

	private String value;

	Mechmode(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
