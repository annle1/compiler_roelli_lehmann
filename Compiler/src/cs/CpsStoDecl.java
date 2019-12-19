package cs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import token.IToken;

//cpsStoDecl ::= stoDecl cpsStoDeclNTS
public class CpsStoDecl implements ICpsStoDecl {
	private final IStoDecl N_stoDecl;
	private final ICpsStoDeclNTS N_cpsStoDeclNTS;
	
	public CpsStoDecl(
			IStoDecl N_stoDecl,
			ICpsStoDeclNTS N_cpsStoDeclNTS) {
		this.N_stoDecl = N_stoDecl;
		this.N_cpsStoDeclNTS = N_cpsStoDeclNTS;
	}

	@Override
	public ArrayList<as.StoDecl> toAbstractSyntax() {
		ArrayList<as.StoDecl> tmp = new ArrayList<>();
		tmp.add(N_stoDecl.toAbstractSyntax());
		return N_cpsStoDeclNTS.toAbstractSyntax(tmp);
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
