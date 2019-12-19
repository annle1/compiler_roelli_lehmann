package cs;

import java.lang.reflect.Field;

import token.IToken;

// decl ::= procDecl
public class DeclProc implements IDecl {
	private final IProcDecl N_procDecl;
	
	public DeclProc(
			IProcDecl N_procDecl) {
		this.N_procDecl = N_procDecl;
	}
	
	@Override
	public as.IDecl toAbstractSyntax() {
		return N_procDecl.toAbstractSyntax();
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
