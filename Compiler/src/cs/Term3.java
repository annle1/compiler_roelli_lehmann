package cs;

import java.lang.reflect.Field;

import as.IExpr;
import token.IToken;

// term3 ::= factor term3NTS
public class Term3 implements ITerm3 {
	private final IFactor N_factor;
	private final ITerm3NTS N_term3NTS;
	
	public Term3(
			IFactor N_factor,
			ITerm3NTS N_term3NTS) {
		this.N_factor = N_factor;
		this.N_term3NTS = N_term3NTS;
	}
	
	@Override
	public IExpr toAbstractSyntax() {
		return N_term3NTS.toAbstractSyntax(N_factor.toAbstractSyntax());
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
