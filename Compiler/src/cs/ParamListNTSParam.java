package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

// paramListNTS ::= param paramNTS
public class ParamListNTSParam implements IParamListNTS {
	private final IParam N_param;
	private final IParamNTS N_paramNTS;
	
	public ParamListNTSParam(
			IParam N_param,
			IParamNTS N_paramNTS) {
		this.N_param = N_param;
		this.N_paramNTS = N_paramNTS;
	}

	@Override
	public ArrayList<as.Param> toAbstractSyntax() {
		ArrayList<as.Param> tmp = new ArrayList<>();
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
