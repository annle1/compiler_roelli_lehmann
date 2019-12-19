package cs;

import java.lang.reflect.Field;

import token.Changemode;
import token.IToken;

// changeModeNTS::= ε
public class ChangeModeNTSEpsilon implements IChangeModeNTS {
	public ChangeModeNTSEpsilon() {
		// do nothing
	}

	@Override
	public Changemode toAbstractSyntax() {
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
