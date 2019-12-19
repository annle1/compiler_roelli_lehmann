package cs;

import java.lang.reflect.Field;

import as.IExpr;
import token.IToken;

//term1 ::= term2 term1NTS
public class Term1 implements ITerm1 {
	private final ITerm2 N_term2;
	private final ITerm1NTS N_term1NTS;
	
	public Term1(
			ITerm2 N_term2,
			ITerm1NTS N_term1NTS) {
		this.N_term2 = N_term2;
		this.N_term1NTS = N_term1NTS;
	}

	@Override
	public IExpr toAbstractSyntax() {
		return N_term1NTS.toAbstractSyntax(N_term2.toAbstractSyntax());
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
