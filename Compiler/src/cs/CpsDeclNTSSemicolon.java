package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

// cpsDeclNTS ::= SEMICOLON decl cpsDeclNTS
public class CpsDeclNTSSemicolon implements ICpsDeclNTS{
	private final IToken T_semicolon;
	private final IDecl N_decl;
	private final ICpsDeclNTS N_cpsDeclNTS;
	
	public CpsDeclNTSSemicolon(
			IToken T_semicolon,
			IDecl N_decl,
			ICpsDeclNTS N_cpsDeclNTS) {
		this.T_semicolon = T_semicolon;
		this.N_decl = N_decl;
		this.N_cpsDeclNTS = N_cpsDeclNTS;
	}
	
	@Override
	public ArrayList<as.IDecl> toAbstractSyntax(ArrayList<as.IDecl> tmp) {
		tmp.add(N_decl.toAbstractSyntax());
		return N_cpsDeclNTS.toAbstractSyntax(tmp);
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
