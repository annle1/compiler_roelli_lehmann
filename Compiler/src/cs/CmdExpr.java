package cs;

import java.lang.reflect.Field;

import token.IToken;

//cmd ::= expr BECOMES expr
public class CmdExpr implements ICmd {
	private final IExpr N_expr1;
	private final IToken T_becomes;
	private final IExpr N_expr2;
	
	public CmdExpr(
			IExpr N_expr1,
			IToken T_becomes,
			IExpr N_expr2) {
		this.N_expr1 = N_expr1;
		this.T_becomes = T_becomes;
		this.N_expr2 = N_expr2;
	}

	@Override
	public as.ICmd toAbstractSyntax() {
		return new as.AssignCmd(N_expr1.toAbstractSyntax(), N_expr2.toAbstractSyntax());
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
