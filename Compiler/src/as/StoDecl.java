package as;

import java.util.ArrayList;
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
import util.DeepCopyHelper;
import vm.ICodeArray;
import vm.ICodeArray.CodeTooSmallError;
import vm.IInstructions;
import vm.VirtualMachine;
import vm.IInstructions.IExecInstr;
import vm.IInstructions.IInstr;

public class StoDecl extends AbsSynTreeNode implements IDecl{
	private Changemode changeMode;
	private TypedIdent typedIdent;
	
	public StoDecl(Changemode changeMode, TypedIdent typedIdent) {
		this.changeMode = changeMode;
		this.typedIdent = typedIdent;
		// Set the const boolean value on the typedIdent to true
		if(changeMode == Changemode.CONST)
			this.typedIdent.setConst();
	}
	
	public StoDecl(TypedIdent typedIdent) {
		this.typedIdent = typedIdent;
	}

	public TypedIdent getTypedIdent() {
		return typedIdent;
	}
	
	@Override
	public String getIdentString() {
		return typedIdent.getValue();
	}	
		
	@Override
	public void saveNamespaceInfoToNode(HashMap<String, TypedIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError {
		// Do nothing		
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
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly) throws CodeTooSmallError {
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
		if(changeMode != null)
			s += argumentIndent + "<changeMode>: " + changeMode.toString() + "\n";
		s += argumentIndent + "<typedIdent>:\n";
		s += typedIdent.toString(subIndent);
		
		return s;
	}	
}
