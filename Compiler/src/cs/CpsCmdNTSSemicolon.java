package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

// cpsCmdNTS ::= SEMICOLON cmd cpsCmdNTS
public class CpsCmdNTSSemicolon implements ICpsCmdNTS {
	private final IToken T_semicolon;
	private final ICmd N_cmd;
	private final ICpsCmdNTS N_cpsCmdNTS;
	
	public CpsCmdNTSSemicolon(
			IToken T_semicolon,
			ICmd N_cmd,
			ICpsCmdNTS N_cpsCmdNTS) {
		this.T_semicolon = T_semicolon;
		this.N_cmd = N_cmd;
		this.N_cpsCmdNTS = N_cpsCmdNTS;
	}

	@Override
	public ArrayList<as.ICmd> toAbstractSyntax(ArrayList<as.ICmd> tmp) {
		tmp.add(N_cmd.toAbstractSyntax());
		return N_cpsCmdNTS.toAbstractSyntax(tmp);
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
