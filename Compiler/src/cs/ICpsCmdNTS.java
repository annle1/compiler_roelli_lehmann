package cs;

import java.util.ArrayList;

public interface ICpsCmdNTS extends IProductions {

	ArrayList<as.ICmd> toAbstractSyntax(ArrayList<as.ICmd> tmp);

}
