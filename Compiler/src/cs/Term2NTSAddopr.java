package cs;

import java.lang.reflect.Field;

import as.IExpr;
import token.Addopr;
import token.IToken;

//term2NTS ::= ADDOPR term3 term2NTS
public class Term2NTSAddopr implements ITerm2NTS {
	private final IToken T_addopr;
	private final ITerm3 N_term3;
	private final ITerm2NTS N_term2NTS;
	
	public Term2NTSAddopr(
			IToken T_addopr,
			ITerm3 N_term3,
			ITerm2NTS N_term2NTS) {
		this.T_addopr = T_addopr;
		this.N_term3 = N_term3;
		this.N_term2NTS = N_term2NTS;
	}
	
	@Override
	public IExpr toAbstractSyntax(IExpr expr) {
		as.IExpr expr2 = new as.AddExpr((Addopr)T_addopr, expr, N_term3.toAbstractSyntax());
		return N_term2NTS.toAbstractSyntax(expr2);
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
