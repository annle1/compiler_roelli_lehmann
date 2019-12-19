package cs;

import java.lang.reflect.Field;

import token.Changemode;
import token.IToken;

// changeModeNTS ::= CHANGEMODE
public class ChangeModeNTSChangeMode implements IChangeModeNTS {
	private final IToken T_changemode;
	
	public ChangeModeNTSChangeMode(
			IToken T_changemode) {
		this.T_changemode = T_changemode;
	}

	@Override
	public Changemode toAbstractSyntax() {
		return (Changemode)T_changemode;
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
