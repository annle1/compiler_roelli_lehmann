package cs;

import java.lang.reflect.Field;

import token.IToken;

//factor ::= monadicOpr factor
public class FactorMonadicopr implements IFactor {
	private final IMonadicOpr N_monadicOpr;
	private final IFactor N_factor;
	
	public FactorMonadicopr(
			IMonadicOpr N_monadicOpr,
			IFactor N_factor) {
		this.N_monadicOpr = N_monadicOpr;
		this.N_factor = N_factor;
	}

	@Override
	public as.IFactor toAbstractSyntax() {
		return new as.MonadicFactor(N_monadicOpr.toAbstractSyntax(), N_factor.toAbstractSyntax());
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
