package cs;

import java.lang.reflect.Field;

import token.IToken;

// param ::= mechModeNTS changeModeNTS typedIdent
public class Param implements IParam {
	private final IMechModeNTS N_mechModeNTS;
	private final IChangeModeNTS N_changeModeNTS;
	private final ITypedIdent N_typedIdent;
	
	public Param(
			IMechModeNTS N_mechModeNTS,
			IChangeModeNTS N_changeModeNTS,
			ITypedIdent N_typedIdent) {
		this.N_mechModeNTS = N_mechModeNTS;
		this.N_changeModeNTS = N_changeModeNTS;
		this.N_typedIdent = N_typedIdent;
	}

	@Override
	public as.Param toAbstractSyntax() {
		return new as.Param(N_mechModeNTS.toAbstractSyntax(), N_changeModeNTS.toAbstractSyntax(), N_typedIdent.toAbstractSyntax());
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
