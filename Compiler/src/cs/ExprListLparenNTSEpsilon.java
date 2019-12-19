package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

//exprListLparenNTS ::= ε	
public class ExprListLparenNTSEpsilon implements IExprListLparenNTS {
	public ExprListLparenNTSEpsilon() {
		// do nothing
	}

	@Override
	public ArrayList<as.IExpr> toAbstractSyntax() {
		return new ArrayList<>();
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
