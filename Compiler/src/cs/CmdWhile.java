package cs;

import java.lang.reflect.Field;

import token.IToken;

//cmd ::= WHILE expr DO cpsCmd ENDWHILE	
public class CmdWhile implements ICmd {
	private final IToken T_while;
	private final IExpr N_expr;
	private final IToken T_do;
	private final ICpsCmd N_cpsCmd;
	private final IToken T_endwhile;
	
	@Override
	public as.ICmd toAbstractSyntax() {
		return new as.WhileCmd(N_expr.toAbstractSyntax(), N_cpsCmd.toAbstractSyntax());
	}

	public CmdWhile(
			IToken T_while,
			IExpr N_expr,
			IToken T_do,
			ICpsCmd N_cpsCmd,
			IToken T_endwhile) {
		this.T_while = T_while;
		this.N_expr = N_expr;
		this.T_do = T_do;
		this.N_cpsCmd = N_cpsCmd;
		this.T_endwhile = T_endwhile;
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
