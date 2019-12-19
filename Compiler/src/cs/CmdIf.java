package cs;

import java.lang.reflect.Field;

import token.IToken;

//cmd ::= IF expr THEN cpsCmd ifelseNTS ENDIF
public class CmdIf implements ICmd {
	private final IToken T_if;
	private final IExpr N_expr;
	private final IToken T_then;
	private final ICpsCmd N_cpsCmd;
	private final IIfelseNTS N_ifelseNTS;
	private final IToken T_endif;
	
	public CmdIf(
			IToken T_if,
			IExpr N_expr,
			IToken T_then,
			ICpsCmd N_cpsCmd,
			IIfelseNTS N_ifelseNTS,
			IToken T_endif) {
		this.T_if = T_if;
		this.N_expr = N_expr;
		this.T_then = T_then;
		this.N_cpsCmd = N_cpsCmd;
		this.N_ifelseNTS = N_ifelseNTS;
		this.T_endif = T_endif;
	}

	@Override
	public as.ICmd toAbstractSyntax() {
		return new as.IfCmd(N_expr.toAbstractSyntax(), N_cpsCmd.toAbstractSyntax(), N_ifelseNTS.toAbstractSyntax());
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
