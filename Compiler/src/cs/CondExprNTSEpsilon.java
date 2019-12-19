package cs;

import java.lang.reflect.Field;

import as.IExpr;
import token.IToken;

//condExprNTS ::= ε 	
public class CondExprNTSEpsilon implements ICondExprNTS {
	public CondExprNTSEpsilon() {
		// do nothing
	}
	
	@Override
	public IExpr toAbstractSyntax(IExpr expr) {
		return expr;
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
