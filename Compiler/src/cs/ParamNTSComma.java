package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

// paramNTS ::= COMMA param paramNTS
public class ParamNTSComma implements IParamNTS {
	private final IToken T_comma;
	private final IParam N_param;
	private final IParamNTS N_paramNTS;
	
	public ParamNTSComma(
			IToken T_comma,
			IParam N_param,
			IParamNTS N_paramNTS) {
		this.T_comma = T_comma;
		this.N_param = N_param;
		this.N_paramNTS = N_paramNTS;
	}

	@Override
	public ArrayList<as.Param> toAbstractSyntax(ArrayList<as.Param> tmp) {
		tmp.add(N_param.toAbstractSyntax());
		return N_paramNTS.toAbstractSyntax(tmp);
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
