package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

//procDeclNTS ::= LOCAL cpsStoDecl
public class ProcDeclNTSLocal implements IProcDeclNTS {
	private final IToken T_local;
	private final ICpsStoDecl N_cpsStoDecl;
	
	public ProcDeclNTSLocal(
			IToken T_local,
			ICpsStoDecl N_cpsStoDecl) {
		this.T_local = T_local;
		this.N_cpsStoDecl = N_cpsStoDecl;
	}

	@Override
	public ArrayList<as.StoDecl> toAbstractSyntax() {
		return N_cpsStoDecl.toAbstractSyntax();
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
