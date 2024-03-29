package as;

import java.util.HashMap;
import java.util.stream.Collectors;

import exception.AlreadyInitializedError;
import exception.CannotAssignToConstError;
import exception.CaseAlreadyDeclaredError;
import exception.DefaultCaseBoolOverkillError;
import exception.GlobalInitializationProhibitedError;
import exception.NameAlreadyDeclaredError;
import exception.NameNotDeclaredError;
import exception.NotInitializedError;
import exception.TypeCheckError;
import token.LRVal;
import token.Literal;
import token.Type;
import util.DeepCopyHelper;
import vm.IInstructions;
import vm.ICodeArray.CodeTooSmallError;

public class LiteralFactor extends AbsSynTreeNode implements IFactor{
	private Literal literal;

	public LiteralFactor(Literal literal) {
		this.literal = literal;
	}	
	
	@Override
	public void saveNamespaceInfoToNode(HashMap<String, TypedIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError {
	}
	
	@Override
	public LRVal getLRValue() {
		return LRVal.RVAL;
	}

	@Override
	public Type getType() {
		return literal.getType();
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		// Do nothing
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		// Do nothing		
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		if(!simulateOnly) {
			if(literal.getType() == Type.BOOL) {
				codeArray.put(codeArrayPointer, new IInstructions.LoadImBool(literal.getBoolValue()));
			} else if (literal.getType() == Type.INT64) {
				codeArray.put(codeArrayPointer, new IInstructions.LoadImInt(literal.getIntValue()));
			} else {
				throw new RuntimeException("WTF, unknown type found????");
			}
		}
		codeArrayPointer++;
	}

	@Override
	public String toString(String indent) {
		String nameIndent = indent;
		String argumentIndent = indent + " ";
		String s = "";
		s += nameIndent + this.getClass().getName() + "\n";
		if(localStoresNamespace != null)
			s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream().map(Object::toString).collect(Collectors.joining(",")) + "\n";		
		s += argumentIndent + "<literal>: " + literal.toString() + "\n";
		
		return s;
	}	
}
