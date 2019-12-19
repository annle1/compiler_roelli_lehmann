package cs;

import java.lang.reflect.Field;

import as.IExpr;
import token.IToken;

// term2 ::= term3 term2NTS
public class Term2 implements ITerm2 {
	private final ITerm3 N_term3;
	private final ITerm2NTS N_term2NTS;
	
	public Term2(
			ITerm3 N_term3,
			ITerm2NTS N_term2NTS) {
		this.N_term3 = N_term3;
		this.N_term2NTS = N_term2NTS;
	}

	@Override
	public IExpr toAbstractSyntax() {
		return N_term2NTS.toAbstractSyntax(N_term3.toAbstractSyntax());
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
