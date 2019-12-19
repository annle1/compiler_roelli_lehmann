package cs;

import java.lang.reflect.Field;

import token.IToken;

// decl ::= stoDecl
public class DeclSto implements IDecl {
	private final IStoDecl N_stoDecl;
	
	public DeclSto(
			IStoDecl N_stoDecl) {
		this.N_stoDecl = N_stoDecl;
	}
	
	@Override
	public as.IDecl toAbstractSyntax() {
		return N_stoDecl.toAbstractSyntax();
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
