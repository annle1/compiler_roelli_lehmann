package as;

import java.util.HashMap;
import java.util.stream.Collectors;

import exception.AlreadyInitializedError;
import exception.CannotAssignToConstError;
import exception.CaseAlreadyDeclaredError;
import exception.DefaultCaseBoolOverkillError;
import exception.GlobalInitializationProhibitedError;
import exception.InvalidParamCountError;
import exception.LRValError;
import exception.NameAlreadyDeclaredError;
import exception.NameAlreadyGloballyDeclaredError;
import exception.NameNotDeclaredError;
import exception.NotInitializedError;
import exception.TypeCheckError;
import token.LRVal;
import token.Relopr;
import token.Type;
import util.DeepCopyHelper;
import vm.IInstructions;
import vm.ICodeArray.CodeTooSmallError;

public class RelExpr extends AbsSynTreeNode implements IExpr{
	private Relopr relOpr;
	private IExpr exprLeft;
	private IExpr exprRight;
	
	public RelExpr(Relopr relOpr, IExpr exprLeft, IExpr exprRight) {
		this.relOpr = relOpr;
		this.exprLeft = exprLeft;
		this.exprRight = exprRight;
	}
	
	@Override
	public void saveNamespaceInfoToNode(HashMap<String, TypedIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		exprLeft.saveNamespaceInfoToNode(this.localStoresNamespace);
		exprRight.saveNamespaceInfoToNode(this.localStoresNamespace);
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValError, InvalidParamCountError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError {
		exprLeft.doScopeChecking();
		exprRight.doScopeChecking();
	}
	
	@Override
	public LRVal getLRValue() {
		return LRVal.RVAL;
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		exprLeft.doTypeChecking();
		exprRight.doTypeChecking();
		
		if(exprLeft.getType() == Type.BOOL)
			throw new TypeCheckError(Type.INT64, exprLeft.getType());
		if(exprLeft.getType() != exprRight.getType())
			throw new TypeCheckError(exprLeft.getType(), exprRight.getType());
	}

	@Override
	public Type getType() {
		return Type.BOOL;
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		exprLeft.doInitChecking(globalProtected);
		exprRight.doInitChecking(globalProtected);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		
		exprLeft.addIInstrToCodeArray(localLocations, simulateOnly);
		exprRight.addIInstrToCodeArray(localLocations, simulateOnly);
		
		if(!simulateOnly) {
			switch(relOpr) {
				case EQ:
					codeArray.put(codeArrayPointer, new IInstructions.EqInt());
					break;
				case GE:
					codeArray.put(codeArrayPointer, new IInstructions.GeInt());
					break;
				case GT:
					codeArray.put(codeArrayPointer, new IInstructions.GtInt());
					break;
				case LE:
					codeArray.put(codeArrayPointer, new IInstructions.LeInt());
					break;
				case LT:
					codeArray.put(codeArrayPointer, new IInstructions.LtInt());
					break;	
				case NE:
					codeArray.put(codeArrayPointer, new IInstructions.NeInt());
					break;					
			}
		}
		codeArrayPointer++;		
	}

	@Override
	public String toString(String indent) {
		String nameIndent = indent;
		String argumentIndent = indent + " ";
		String subIndent = indent + "  ";
		String s = "";
		s += nameIndent + this.getClass().getName() + "\n";
		if(localStoresNamespace != null)
			s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream().map(Object::toString).collect(Collectors.joining(",")) + "\n";		
		s += argumentIndent + "<relOpr>: " + relOpr.toString() + "\n";
		s += argumentIndent + "<exprLeft>:\n";
		s += exprLeft.toString(subIndent);
		s += argumentIndent + "<exprRight>:\n";
		s += exprRight.toString(subIndent);
		
		return s;
	}	
}
