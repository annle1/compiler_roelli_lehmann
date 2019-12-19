package cs;

import java.lang.reflect.Field;

import token.IToken;
import token.Mechmode;

// mechModeNTS ::= MECHMODE
public class MechModeNTSMechMode implements IMechModeNTS {
	private final IToken T_mechmode;
	
	public MechModeNTSMechMode(
			IToken T_mechmode) {
		this.T_mechmode = T_mechmode;
	}

	@Override
	public Mechmode toAbstractSyntax() {
		return (Mechmode)T_mechmode;
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
