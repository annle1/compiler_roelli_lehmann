package token;

public enum Relopr implements IToken{
	EQ("(RELOPR, EQ)"),
	NE("(RELOPR, NE)"),
	LT("(RELOPR, LT)"),
	GT("(RELOPR, GT)"),
	LE("(RELOPR, LE)"),
	GE("(RELOPR, GE)");

	private String value;

	Relopr(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
