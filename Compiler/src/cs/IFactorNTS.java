package cs;

import token.Ident;

public interface IFactorNTS extends IProductions {

	as.IFactor toAbstractSyntax(Ident ident);

}
