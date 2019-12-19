package cs;

import java.lang.reflect.Field;

import token.IToken;

// stoDecl ::= typedIdent
public class StoDeclTypedIdent implements IStoDecl {
	private final ITypedIdent N_typedIdent;
	
	public StoDeclTypedIdent(
			ITypedIdent N_typedIdent) {
		this.N_typedIdent = N_typedIdent;
	}

	@Override
	public as.StoDecl toAbstractSyntax() {
		return new as.StoDecl(N_typedIdent.toAbstractSyntax());
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
