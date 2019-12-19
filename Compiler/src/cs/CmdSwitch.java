package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

//cmd ::= SWITCH expr caseNTS defaultCaseNTS ENDSWITCH	
public class CmdSwitch implements ICmd {
	private final IToken T_switch;
	private final IExpr N_expr;
	private final ICaseNTS N_caseNTS;
	private final IDefaultCaseNTS N_defaultCaseNTS;
	private final IToken T_endswitch;
	
	@Override
	public as.ICmd toAbstractSyntax() {
		ArrayList<as.Case> tmp = new ArrayList<>();
		return new as.SwitchCmd(N_expr.toAbstractSyntax(), N_caseNTS.toAbstractSyntax(tmp), N_defaultCaseNTS.toAbstractSyntax());
	}

	public CmdSwitch(
			IToken T_switch,
			IExpr N_expr,
			ICaseNTS N_caseNTS,
			IDefaultCaseNTS N_defaultCaseNTS,
			IToken T_endswitch) {
		this.T_switch = T_switch;
		this.N_expr = N_expr;
		this.N_caseNTS = N_caseNTS;
		this.N_defaultCaseNTS = N_defaultCaseNTS;
		this.T_endswitch = T_endswitch;
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
