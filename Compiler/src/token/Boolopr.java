package token;

public enum Boolopr implements IToken{
	AND("(BOOLOPR, AND)"),
	OR("(BOOLOPR, OR)"),
	CAND("(BOOLOPR, CAND)"),
	COR("(BOOLOPR, COR)");

	private String value;

	Boolopr(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}