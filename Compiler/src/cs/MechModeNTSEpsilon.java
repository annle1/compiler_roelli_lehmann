package cs;

import java.lang.reflect.Field;

import token.IToken;
import token.Mechmode;

// mechModeNTS::= ε
public class MechModeNTSEpsilon implements IMechModeNTS {
	public MechModeNTSEpsilon() {
		// do nothing
	}

	@Override
	public Mechmode toAbstractSyntax() {
		return null;
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
