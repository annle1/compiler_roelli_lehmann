package cs;

import java.lang.reflect.Field;

import token.IToken;

//expr ::= term0 condExprNTS
public class Expr implements IExpr {
	private final ITerm0 N_term0;
	private final ICondExprNTS N_condExprNTS;
	
	public Expr(
			ITerm0 N_term0,
			ICondExprNTS N_condExprNTS) {
		this.N_term0 = N_term0;
		this.N_condExprNTS = N_condExprNTS;
	}
	
	@Override
	public as.IExpr toAbstractSyntax(){
		return N_condExprNTS.toAbstractSyntax(N_term0.toAbstractSyntax());
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
