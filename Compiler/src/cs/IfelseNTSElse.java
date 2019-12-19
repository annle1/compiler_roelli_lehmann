package cs;

import java.lang.reflect.Field;

import token.IToken;

//ifelseNTS ::= ELSE cpsCmd
public class IfelseNTSElse implements IIfelseNTS {
	private final IToken T_else;
	private final ICpsCmd N_cpsCmd;
	
	public IfelseNTSElse(
			IToken T_else,
			ICpsCmd N_cpsCmd) {
		this.T_else = T_else;
		this.N_cpsCmd = N_cpsCmd;
	}

	@Override
	public as.CpsCmd toAbstractSyntax() {
		return N_cpsCmd.toAbstractSyntax();
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
