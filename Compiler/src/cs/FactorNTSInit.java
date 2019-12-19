package cs;

import java.lang.reflect.Field;

import token.IToken;
import token.Ident;

//factorNTS ::= INIT
public class FactorNTSInit implements IFactorNTS {
	private final IToken T_init;
	
	public FactorNTSInit(
			IToken T_init) {
		this.T_init = T_init;
	}

	@Override
	public as.IFactor toAbstractSyntax(Ident ident) {
		return new as.InitFactor(ident, true);
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
