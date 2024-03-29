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
import token.Monadicopr;
import token.Type;
import util.DeepCopyHelper;
import vm.IInstructions;
import vm.ICodeArray.CodeTooSmallError;

public class MonadicFactor extends AbsSynTreeNode implements IFactor{
	private Monadicopr monadicOpr;
	private IFactor factor;
	
	public MonadicFactor(Monadicopr monadicOpr, IFactor factor) {
		this.monadicOpr = monadicOpr;
		this.factor = factor;
	}

	public MonadicFactor(Monadicopr monadicOpr) {
		this.monadicOpr = monadicOpr;
	}
	
	@Override
	public void saveNamespaceInfoToNode(HashMap<String, TypedIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		factor.saveNamespaceInfoToNode(this.localStoresNamespace);
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValError, InvalidParamCountError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError {
		factor.doScopeChecking();
	}
	
	@Override
	public LRVal getLRValue() {
		return LRVal.RVAL;
	}

	@Override
	public Type getType() {
		return factor.getType();
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		factor.doTypeChecking();
		
		if(monadicOpr.getOperator() == token.Token.NOTOPR && factor.getType() != Type.BOOL)
			throw new TypeCheckError(Type.BOOL, factor.getType());
		if(monadicOpr.getOperator() instanceof token.Addopr && factor.getType() != Type.INT64)
			throw new TypeCheckError(Type.INT64, factor.getType());
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		factor.doInitChecking(globalProtected);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		
		// Add the value on top of stack
		factor.addIInstrToCodeArray(localLocations, simulateOnly);
		
		// Negate it
		if(!simulateOnly) {
			if(monadicOpr.getOperator() == token.Token.NOTOPR) {
				codeArray.put(codeArrayPointer, new IInstructions.NegBool());
			} else if(monadicOpr.getOperator() instanceof token.Addopr && (token.Addopr)monadicOpr.getOperator() == token.Addopr.MINUS) {
				codeArray.put(codeArrayPointer, new IInstructions.NegInt());		
			} else {
				throw new RuntimeException("UNSUPPORTED MONADIC OPERATOR!");
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
		s += argumentIndent + "<monadicOpr>: " + monadicOpr.toString() + "\n";
		s += argumentIndent + "<factor>:\n";
		s += factor.toString(subIndent);
		
		return s;
	}	
}
