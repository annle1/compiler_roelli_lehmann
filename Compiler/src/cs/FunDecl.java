package cs;

import java.lang.reflect.Field;

import token.IToken;

// funDecl ::= FUN IDENT paramList RETURNS stoDecl funDeclNTS DO cpsCmd ENDFUN
public class FunDecl implements IFunDecl {
	private final IToken T_fun;
	private final IToken T_ident;
	private final IParamList N_paramList;
	private final IToken T_returns;
	private final IStoDecl N_stoDecl;
	private final IFunDeclNTS N_funDeclNTS;
	private final IToken T_do;
	private final ICpsCmd N_cpsCmd;
	private final IToken T_endfun;
	
	public FunDecl(
			IToken T_fun,
			IToken T_ident,
			IParamList N_paramList,
			IToken T_returns,
			IStoDecl N_stoDecl,
			IFunDeclNTS N_funDeclNTS,
			IToken T_do,
			ICpsCmd N_cpsCmd,
			IToken T_endfun) {
		this.T_fun = T_fun;
		this.T_ident = T_ident;
		this.N_paramList = N_paramList;
		this.T_returns = T_returns;
		this.N_stoDecl = N_stoDecl;
		this.N_funDeclNTS = N_funDeclNTS;
		this.T_do = T_do;
		this.N_cpsCmd = N_cpsCmd;
		this.T_endfun = T_endfun;
	}

	@Override
	public as.IDecl toAbstractSyntax() {
		return new as.FunDecl((token.Ident)T_ident, N_paramList.toAbstractSyntax(), N_stoDecl.toAbstractSyntax(), N_funDeclNTS.toAbstractSyntax(), N_cpsCmd.toAbstractSyntax());
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
