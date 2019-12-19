package cs;

import java.lang.reflect.Field;

import token.Changemode;
import token.IToken;

// stoDecl ::= CHANGEMODE typedIdent
public class StoDeclChangemode implements IStoDecl {
	private final IToken T_changemode;
	private final ITypedIdent N_typedIdent;
	
	public StoDeclChangemode(
			IToken T_changemode,
			ITypedIdent N_typedIdent) {
		this.T_changemode = T_changemode;
		this.N_typedIdent = N_typedIdent;
	}
	
	@Override
	public as.StoDecl toAbstractSyntax() {
		return new as.StoDecl((Changemode)T_changemode, N_typedIdent.toAbstractSyntax());
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
