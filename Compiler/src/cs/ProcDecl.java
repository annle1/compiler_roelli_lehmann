package cs;

import java.lang.reflect.Field;

import token.IToken;

// procDecl ::= PROC IDENT paramList procDeclNTS DO cpsCmd ENDPROC
public class ProcDecl implements IProcDecl {
	private final IToken T_proc;
	private final IToken T_ident;
	private final IParamList N_paramList;
	private final IProcDeclNTS N_procDeclNTS;
	private final IToken T_do;
	private final ICpsCmd N_cpsCmd;
	private final IToken T_endproc;
	
	@Override
	public as.IDecl toAbstractSyntax() {
		return new as.ProcDecl((token.Ident)T_ident, N_paramList.toAbstractSyntax(), N_procDeclNTS.toAbstractSyntax(), N_cpsCmd.toAbstractSyntax());
	}

	public ProcDecl(
			IToken T_proc,
			IToken T_ident,
			IParamList N_paramList,
			IProcDeclNTS N_procDeclNTS,
			IToken T_do,
			ICpsCmd N_cpsCmd,
			IToken T_endproc) {
		this.T_proc = T_proc;
		this.T_ident = T_ident;
		this.N_paramList = N_paramList;
		this.N_procDeclNTS = N_procDeclNTS;
		this.T_do = T_do;
		this.N_cpsCmd = N_cpsCmd;
		this.T_endproc = T_endproc;
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
