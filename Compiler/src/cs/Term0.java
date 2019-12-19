package cs;

import java.lang.reflect.Field;

import token.IToken;

//term0 ::= term1 term0NTS
public class Term0 implements ITerm0 {
	private final ITerm1 N_term1;
	private final ITerm0NTS N_term0NTS;
	
	public Term0(
			ITerm1 N_term1,
			ITerm0NTS N_term0NTS) {
		this.N_term1 = N_term1;
		this.N_term0NTS = N_term0NTS;
	}
	
	@Override
	public as.IExpr toAbstractSyntax(){
		return N_term0NTS.toAbstractSyntax(N_term1.toAbstractSyntax());
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
