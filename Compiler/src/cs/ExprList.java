package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

//exprList ::= LPAREN exprListLparenNTS RPAREN
public class ExprList implements IExprList {
	private final IToken T_lparen;
	private final IExprListLparenNTS N_exprListLparenNTS;
	private final IToken T_rparen;
	
	public ExprList(
			IToken T_lparen,
			IExprListLparenNTS N_exprListLparenNTS,
			IToken T_rparen) {
		this.T_lparen = T_lparen;
		this.N_exprListLparenNTS = N_exprListLparenNTS;
		this.T_rparen = T_rparen;
	}

	@Override
	public ArrayList<as.IExpr> toAbstractSyntax() {
		return N_exprListLparenNTS.toAbstractSyntax();		
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
