package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

//exprListLparenNTS ::= expr exprListNTS
public class ExprListLparenNTSExpr implements IExprListLparenNTS {
	private final IExpr N_expr;
	private final IExprListNTS N_exprListNTS;
	
	public ExprListLparenNTSExpr(
			IExpr N_expr,
			IExprListNTS N_exprListNTS) {
		this.N_expr = N_expr;
		this.N_exprListNTS = N_exprListNTS;
	}

	@Override
	public ArrayList<as.IExpr> toAbstractSyntax() {
		ArrayList<as.IExpr> tmp = new ArrayList<>();
		tmp.add(N_expr.toAbstractSyntax());
		return N_exprListNTS.toAbstractSyntax(tmp);		
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
