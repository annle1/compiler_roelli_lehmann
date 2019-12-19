package cs;

import java.lang.reflect.Field;

import token.IToken;

// factor ::= LITERAL
public class FactorLiteral implements IFactor {
	private final IToken T_literal;
	
	public FactorLiteral(
			IToken T_literal) {
		this.T_literal = T_literal;
	}

	@Override
	public as.IFactor toAbstractSyntax() {
		return new as.LiteralFactor((token.Literal)T_literal);
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
