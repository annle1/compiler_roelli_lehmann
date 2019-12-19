package cs;

import java.lang.reflect.Field;

import token.IToken;

//factor ::= LPAREN expr RPAREN	
public class FactorLParen implements IFactor {
	private final IToken T_lparen;
	private final IExpr N_expr;
	private final IToken T_rparen;
	
	public FactorLParen(
			IToken T_lparen,
			IExpr N_expr,
			IToken T_rparen) {
		this.T_lparen = T_lparen;
		this.N_expr = N_expr;
		this.T_rparen = T_rparen;
	}

	@Override
	public as.IFactor toAbstractSyntax() {
		return new as.ExprFactor(N_expr.toAbstractSyntax());
	}

	@Override
	public String toString(String indent) {
		String subindent = indent + " ";
		String s = "";
		try {		
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field field : fields) {	
				if(field.getType() == IToken.class) {
					s += indent + field.get(this) + "\n";
				} else if (field.get(this) instanceof IProductions) {
					s += ((IProductions)field.get(this)).toString(subindent);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return s;
	}
}
