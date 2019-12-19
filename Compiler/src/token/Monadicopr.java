package token;

public class Monadicopr implements IToken {
	private IToken operator;
	
	public Monadicopr(IToken operator) {
		this.operator = operator; 
	}
	
	public IToken getOperator() {
		return operator;
	}
}
