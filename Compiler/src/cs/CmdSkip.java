package cs;

import java.lang.reflect.Field;

import token.IToken;

//cmd ::= SKIP
public class CmdSkip implements ICmd {
	private final IToken T_skip;
	
	public CmdSkip(
			IToken T_skip) {
		this.T_skip = T_skip;
	}

	@Override
	public as.ICmd toAbstractSyntax() {
		return new as.SkipCmd();
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
