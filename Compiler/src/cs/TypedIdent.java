package cs;

import java.lang.reflect.Field;

import token.IToken;

// typedIdent ::= IDENT COLON TYPE
public class TypedIdent implements ITypedIdent {
	private final IToken T_ident;
	private final IToken T_colon;
	private final IToken T_type;
	
	public TypedIdent(
			IToken T_ident,
			IToken T_colon,
			IToken T_type) {
		this.T_ident = T_ident;
		this.T_colon = T_colon;
		this.T_type = T_type;
	}

	@Override
	public as.TypedIdent toAbstractSyntax() {
		return new as.TypedIdent((token.Ident)T_ident, (token.Type)T_type);
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
