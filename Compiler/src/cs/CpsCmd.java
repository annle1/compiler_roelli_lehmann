package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

// cpsCmd ::= cmd cpsCmdNTS
public class CpsCmd implements ICpsCmd {
	private final ICmd N_cmd;
	private final ICpsCmdNTS N_cpsCmdNTS;
	
	public CpsCmd(
			ICmd N_cmd,
			ICpsCmdNTS N_cpsCmdNTS) {
		this.N_cmd = N_cmd;
		this.N_cpsCmdNTS = N_cpsCmdNTS;
	}

	@Override
	public as.CpsCmd toAbstractSyntax() {
		ArrayList<as.ICmd> tmp = new ArrayList<>();
		tmp.add(N_cmd.toAbstractSyntax());
		
		return new as.CpsCmd(N_cpsCmdNTS.toAbstractSyntax(tmp));
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
