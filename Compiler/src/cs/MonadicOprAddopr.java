package cs;

import java.lang.reflect.Field;

import token.IToken;
import token.Monadicopr;

//monadicOpr ::= ADDOPR	
public class MonadicOprAddopr implements IMonadicOpr {
	private final IToken T_addopr;
	
	public MonadicOprAddopr(
			IToken T_addopr) {
		this.T_addopr = T_addopr;
	}

	@Override
	public Monadicopr toAbstractSyntax() {
		return new Monadicopr(T_addopr);
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
