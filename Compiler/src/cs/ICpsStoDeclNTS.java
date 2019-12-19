package cs;

import java.util.ArrayList;

public interface ICpsStoDeclNTS extends IProductions {

	ArrayList<as.StoDecl> toAbstractSyntax(ArrayList<as.StoDecl> tmp);

}
