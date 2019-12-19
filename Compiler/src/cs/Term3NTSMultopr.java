package cs;

import java.lang.reflect.Field;

import as.IExpr;
import token.IToken;
import token.Multopr;

//term3NTS ::= MULTOPR factor term3NTS
public class Term3NTSMultopr implements ITerm3NTS {
	private final IToken T_multopr;
	private final IFactor N_factor;
	private final ITerm3NTS N_term3NTS;
	
	public Term3NTSMultopr(
			IToken T_multopr,
			IFactor N_factor,
			ITerm3NTS N_term3NTS) {
		this.T_multopr = T_multopr;
		this.N_factor = N_factor;
		this.N_term3NTS = N_term3NTS;
	}
	
	@Override
	public IExpr toAbstractSyntax(IExpr expr) {
		as.IExpr expr2 = new as.MultExpr((Multopr)T_multopr, expr, N_factor.toAbstractSyntax());
		return N_term3NTS.toAbstractSyntax(expr2);
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
