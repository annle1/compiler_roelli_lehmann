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
import token.Changemode;
import token.LRVal;
import token.Mechmode;
import util.DeepCopyHelper;
import vm.IInstructions;
import vm.ICodeArray.CodeTooSmallError;

public class Param extends AbsSynTreeNode {
	private Mechmode mechMode;
	private Changemode changeMode;
	private TypedIdent typedIdent;
	private LRVal lrVal;
	
	public Param(Mechmode mechMode, Changemode changeMode, TypedIdent typedIdent) {
		this.mechMode = mechMode != null ? mechMode : Mechmode.COPY;
		this.changeMode = changeMode != null ? changeMode : Changemode.CONST;
		this.typedIdent = typedIdent;
		this.typedIdent.setInit();
		// Set the const boolean value on the typedIdent to true
		if(changeMode == Changemode.CONST)
			this.typedIdent.setConst();		
		lrVal = this.mechMode == token.Mechmode.COPY ? LRVal.RVAL : LRVal.LVAL;
	}	
	
	public String getIdentString() {
		return typedIdent.getValue();
	}
	
	public TypedIdent getTypedIdent() {
		return typedIdent;
	}
	
	public LRVal getLRValue() {
		return lrVal;
	}
	
	public Mechmode getMechMode() {
		return mechMode;
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
	public void doTypeChecking() throws TypeCheckError {
		// Do nothing
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError,
			AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		// Do nothing
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		if(!simulateOnly)
			codeArray.put(codeArrayPointer, new IInstructions.AllocBlock(1));
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
		if(mechMode != null)
			s += argumentIndent + "<mechMode>: " + mechMode.toString() + "\n";
		if(changeMode != null)
			s += argumentIndent + "<changeMode>: " + changeMode.toString() + "\n";
		s += argumentIndent + "<typedIdent>:\n";
		s += typedIdent.toString(subIndent);
		
		return s;
	}	
}
