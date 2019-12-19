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
import token.Multopr;
import token.Type;
import util.DeepCopyHelper;
import vm.IInstructions;
import vm.ICodeArray.CodeTooSmallError;

public class MultExpr extends AbsSynTreeNode implements IExpr{
	private Multopr multOpr;
	private IExpr exprLeft;
	private IExpr exprRight;
	
	public MultExpr(Multopr multOpr, IExpr exprLeft, IExpr exprRight) {
		this.multOpr = multOpr;
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
		return exprLeft.getType();
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
			switch(multOpr) {
				case TIMES:
					codeArray.put(codeArrayPointer, new IInstructions.MultInt());
					break;
				case DIV_E:
					codeArray.put(codeArrayPointer, new IInstructions.DivEuclInt());
					break;
				case MOD_E:
					codeArray.put(codeArrayPointer, new IInstructions.ModEuclInt());
					break;
				case DIV_T:
					codeArray.put(codeArrayPointer, new IInstructions.DivTruncInt());
					break;
				case MOD_T:
					codeArray.put(codeArrayPointer, new IInstructions.ModTruncInt());
					break;
				case DIV_F:
					codeArray.put(codeArrayPointer, new IInstructions.DivFloorInt());
					break;
				case MOD_F:
					codeArray.put(codeArrayPointer, new IInstructions.ModFloorInt());
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
		s += argumentIndent + "<multOpr>: " + multOpr.toString() + "\n";
		s += argumentIndent + "<exprLeft>:\n";
		s += exprLeft.toString(subIndent);
		s += argumentIndent + "<exprRight>:\n";
		s += exprRight.toString(subIndent);
		
		return s;
	}	
	

}
