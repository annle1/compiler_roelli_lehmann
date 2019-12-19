package cs;

import java.lang.reflect.Field;

import token.IToken;

//factor ::= IDENT factorNTS	
public class FactorIdent implements IFactor {
	private final IToken T_ident;
	private final IFactorNTS N_factorNTS;
	
	public FactorIdent(
			IToken T_ident,
			IFactorNTS N_factorNTS) {
		this.T_ident = T_ident;
		this.N_factorNTS = N_factorNTS;
	}

	@Override
	public as.IFactor toAbstractSyntax() {
		return N_factorNTS.toAbstractSyntax((token.Ident)T_ident);
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
