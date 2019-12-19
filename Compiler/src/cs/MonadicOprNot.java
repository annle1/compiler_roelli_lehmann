package cs;

import java.lang.reflect.Field;

import token.IToken;
import token.Monadicopr;

//monadicOpr ::= NOT
public class MonadicOprNot implements IMonadicOpr {
	private final IToken T_not;
	
	public MonadicOprNot(
			IToken T_not) {
		this.T_not = T_not;
	}

	@Override
	public Monadicopr toAbstractSyntax() {
		return new Monadicopr(T_not);
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
