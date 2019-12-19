package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;
import token.Type;

//caseNTS::= CASE LITERAL COLON cpsCmd ENDCASE caseNTS
public class CaseNTSCase implements ICaseNTS {
	private final IToken T_case;
	private final IToken T_literal;
	private final IToken T_colon;
	private final ICpsCmd N_cpsCmd;
	private final IToken T_endcase;
	private final ICaseNTS N_caseNTS;
	
	public CaseNTSCase(
			IToken T_case,
			IToken T_literal,
			IToken T_colon,
			ICpsCmd N_cpsCmd,
			IToken T_endcase,
			ICaseNTS N_caseNTS) {
		this.T_case = T_case;
		this.T_literal = T_literal;
		this.T_colon = T_colon;
		this.N_cpsCmd = N_cpsCmd;
		this.T_endcase = T_endcase;
		this.N_caseNTS = N_caseNTS;
	}

	@Override
	public ArrayList<as.Case> toAbstractSyntax(ArrayList<as.Case> tmp) {
		tmp.add(new as.Case((token.Literal)T_literal, N_cpsCmd.toAbstractSyntax()));
		return N_caseNTS.toAbstractSyntax(tmp);		
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
