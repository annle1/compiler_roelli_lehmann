package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

// paramList ::= LPAREN paramListNTS RPAREN
public class ParamList implements IParamList {
	private final IToken T_lparen;
	private final IParamListNTS N_paramListNTS;
	private final IToken T_rparen;
	
	public ParamList(
			IToken T_lparen,
			IParamListNTS N_paramListNTS,
			IToken T_rparen) {
		this.T_lparen = T_lparen;
		this.N_paramListNTS = N_paramListNTS;
		this.T_rparen = T_rparen;
	}

	@Override
	public ArrayList<as.Param> toAbstractSyntax() {
		return N_paramListNTS.toAbstractSyntax();
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
