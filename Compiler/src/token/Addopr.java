package token;

public enum Addopr implements IToken{
	PLUS("(ADDOPR, PLUS)"),
	MINUS("(ADDOPR, MINUS)");

	private String value;

	Addopr(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}