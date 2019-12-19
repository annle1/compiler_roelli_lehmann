package cs;

import java.lang.reflect.Field;

import token.IToken;

// decl ::= funDecl
public class DeclFun implements IDecl {
	private final IFunDecl N_funDecl;
	
	public DeclFun(
			IFunDecl N_funDecl) {
		this.N_funDecl = N_funDecl;
	}

	@Override
	public as.IDecl toAbstractSyntax() {
		return N_funDecl.toAbstractSyntax();
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
