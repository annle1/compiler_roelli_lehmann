package cs;

import java.lang.reflect.Field;

import cs.IExpr;
import token.IToken;

//condExprNTS ::= QUESTIONMARK expr COLON expr
public class CondExprNTSQuestionmark implements ICondExprNTS {
	private final IToken T_questionmark;
	private final IExpr N_expr1;
	private final IToken T_colon;
	private final IExpr N_expr2;
	
	public CondExprNTSQuestionmark(
			IToken T_questionmark,
			IExpr N_expr1,
			IToken T_colon,
			IExpr N_expr2) {
		this.T_questionmark = T_questionmark;
		this.N_expr1 = N_expr1;
		this.T_colon = T_colon;
		this.N_expr2 = N_expr2;
	}
	
	@Override
	public as.IExpr toAbstractSyntax(as.IExpr expr) {
		return new as.ConditionalExpr(expr, N_expr1.toAbstractSyntax(), N_expr2.toAbstractSyntax());
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
