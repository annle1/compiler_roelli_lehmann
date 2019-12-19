package cs;

import java.lang.reflect.Field;

import token.IToken;

//cmd ::= DEBUGOUT expr	
public class CmdDebugout implements ICmd {
	private final IToken T_debugout;
	private final IExpr N_expr;
	
	public CmdDebugout(
			IToken T_debugout,
			IExpr N_expr) {
		this.T_debugout = T_debugout;
		this.N_expr = N_expr;
	}

	@Override
	public as.ICmd toAbstractSyntax() {
		return new as.DebugOutCmd(N_expr.toAbstractSyntax());
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
