package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import as.IDecl;
import token.IToken;

// globalNTS ::= GLOBAL cpsDecl
public class GlobalNTSGlobal implements IGlobalNTS{
	private final IToken T_global;
	private final ICpsDecl N_cpsDecl;
	
	public GlobalNTSGlobal (
			IToken T_global,
			ICpsDecl N_cpsDecl) {
		this.T_global = T_global;
		this.N_cpsDecl = N_cpsDecl;
	}
	
	@Override
	public ArrayList<as.IDecl> toAbstractSyntax() {
		return N_cpsDecl.toAbstractSyntax();
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
