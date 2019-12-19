package token;

public class Ident implements IToken{	
	private String value = null;
	
	@Override
	public String toString() {
		return "(IDENT, " + value + ")";
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
