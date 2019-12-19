package cs;

import java.lang.reflect.Field;

import as.IExpr;
import terminal.Terminal;
import token.IToken;
import token.Ident;
import token.Token;

//program ::= PROGRAM IDENT globalNTS DO cpsCmd ENDPROGRAM
public class Program implements IProgram {
	private final IToken T_program;
	private final IToken T_ident;
	private final IGlobalNTS N_globalNTS;
	private final IToken T_do;
	private final ICpsCmd N_cpsCmd;
	private final IToken T_endprogram;
	
	public Program (
			final IToken T_program,
			final IToken T_ident,
			final IGlobalNTS N_globalNTS,
			final IToken T_do,
			final ICpsCmd N_cpsCmd,
			final IToken T_endprogram) {
		this.T_program = T_program;
		this.T_ident = T_ident;
		this.N_globalNTS = N_globalNTS;
		this.T_do = T_do;
		this.N_cpsCmd = N_cpsCmd;
		this.T_endprogram = T_endprogram;
	}

	@Override
	public as.Program toAbstractSyntax() {
		return new as.Program((Ident)T_ident, N_globalNTS.toAbstractSyntax(), N_cpsCmd.toAbstractSyntax());
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
				System.out.println(field.getType());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return s;
	}
}
