package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

//cpsStoDeclNTS ::= SEMICOLON stoDecl cpsStoDeclNTS
public class CpsStoDeclNTSSemicolon implements ICpsStoDeclNTS {
	private final IToken T_semicolon;
	private final IStoDecl N_stoDecl;
	private final ICpsStoDeclNTS N_cpsStoDeclNTS;
	
	public CpsStoDeclNTSSemicolon(
			IToken T_semicolon,
			IStoDecl N_stoDecl,
			ICpsStoDeclNTS N_cpsStoDeclNTS) {
		this.T_semicolon = T_semicolon;
		this.N_stoDecl = N_stoDecl;
		this.N_cpsStoDeclNTS = N_cpsStoDeclNTS;
	}

	@Override
	public ArrayList<as.StoDecl> toAbstractSyntax(ArrayList<as.StoDecl> tmp) {
		tmp.add(N_stoDecl.toAbstractSyntax());
		return N_cpsStoDeclNTS.toAbstractSyntax(tmp);	
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
