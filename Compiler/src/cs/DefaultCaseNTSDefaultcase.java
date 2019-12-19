package cs;

import java.lang.reflect.Field;

import token.IToken;

//defaultCaseNTS ::= DEFAULTCASE COLON cpsCmd ENDCASE
public class DefaultCaseNTSDefaultcase implements IDefaultCaseNTS {
	private final IToken T_defaultcase;
	private final IToken T_colon;
	private final ICpsCmd N_cpsCmd;
	private final IToken T_endcase;
	
	public DefaultCaseNTSDefaultcase(
			IToken T_defaultcase,
			IToken T_colon,
			ICpsCmd N_cpsCmd,
			IToken T_endcase) {
		this.T_defaultcase = T_defaultcase;
		this.T_colon = T_colon;
		this.N_cpsCmd = N_cpsCmd;
		this.T_endcase = T_endcase;
	}

	@Override
	public as.DefaultCase toAbstractSyntax() {
		return new as.DefaultCase(N_cpsCmd.toAbstractSyntax());
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
