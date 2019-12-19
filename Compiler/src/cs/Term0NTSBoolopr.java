package cs;

import java.lang.reflect.Field;

import as.IExpr;
import token.IToken;

// term0NTS ::= BOOLOPR term1 term0NTS
public class Term0NTSBoolopr implements ITerm0NTS {
	private final IToken T_boolopr;
	private final ITerm1 N_term1;
	private final ITerm0NTS N_term0NTS;
	
	public Term0NTSBoolopr(
			IToken T_boolopr,
			ITerm1 N_term1,
			ITerm0NTS N_term0NTS) {
		this.T_boolopr = T_boolopr;
		this.N_term1 = N_term1;
		this.N_term0NTS = N_term0NTS;
	}
	
	@Override
	public IExpr toAbstractSyntax(IExpr expr) {
		as.IExpr expr2 = new as.BoolExpr((token.Boolopr)T_boolopr, expr, N_term1.toAbstractSyntax());
		return N_term0NTS.toAbstractSyntax(expr2);
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
