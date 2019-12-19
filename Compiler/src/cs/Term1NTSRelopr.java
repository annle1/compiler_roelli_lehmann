package cs;

import java.lang.reflect.Field;

import as.IExpr;
import token.IToken;
import token.Relopr;

//term1NTS::= RELOPR term2
public class Term1NTSRelopr implements ITerm1NTS {
	private final IToken T_relopr;
	private final ITerm2 N_term2;
	
	public Term1NTSRelopr(
			IToken T_relopr,
			ITerm2 N_term2) {
		this.T_relopr = T_relopr;
		this.N_term2 = N_term2;
	}

	@Override
	public IExpr toAbstractSyntax(IExpr expr) {
		return new as.RelExpr((Relopr)T_relopr, expr, N_term2.toAbstractSyntax());
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
