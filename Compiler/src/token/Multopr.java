package token;

public enum Multopr implements IToken{
	TIMES("(MULTOPR, TIMES)"),
	DIV_E("(MULTOPR, DIV_E)"),
	MOD_E("(MULTOPR, MOD_E)"),
	DIV_T("(MULTOPR, DIV_T)"),
	MOD_T("(MULTOPR, MOD_T)"),
	DIV_F("(MULTOPR, DIV_F)"),
	MOD_F("(MULTOPR, MOD_F)");

	private String value;

	Multopr(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}