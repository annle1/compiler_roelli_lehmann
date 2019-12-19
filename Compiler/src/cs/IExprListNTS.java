package cs;

import java.util.ArrayList;

public interface IExprListNTS extends IProductions {

	ArrayList<as.IExpr> toAbstractSyntax(ArrayList<as.IExpr> tmp);

}
