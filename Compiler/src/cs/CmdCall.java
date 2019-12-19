package cs;

import java.lang.reflect.Field;

import token.IToken;

//cmd ::= CALL IDENT exprList
public class CmdCall implements ICmd {
	private final IToken T_call;
	private final IToken T_ident;
	private final IExprList N_exprList;
	
	public CmdCall(
			IToken T_call,
			IToken T_ident,
			IExprList N_exprList) {
		this.T_call = T_call;
		this.T_ident = T_ident;
		this.N_exprList = N_exprList;
	}

	@Override
	public as.ICmd toAbstractSyntax() {
		return new as.ProcCallCmd((token.Ident)T_ident, N_exprList.toAbstractSyntax());
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
