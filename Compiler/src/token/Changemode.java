package token;

public enum Changemode implements IToken {
	CONST("(CHANGEMODE, CONST)"),
	VAR("(CHANGEMODE, VAR)");

	private String value;

	Changemode(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}