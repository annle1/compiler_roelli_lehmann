package cs;

import java.lang.reflect.Field;

import token.IToken;
import token.Ident;

//factorNTS ::= exprList
public class FactorNTSExprList implements IFactorNTS {
	private final IExprList N_exprList;
	
	public FactorNTSExprList(
			IExprList N_exprList) {
		this.N_exprList = N_exprList;
	}

	@Override
	public as.IFactor toAbstractSyntax(Ident ident) {
		return new as.FunCallFactor(ident, N_exprList.toAbstractSyntax());
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
