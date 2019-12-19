package as;

import java.util.HashMap;

import vm.ICodeArray;
import vm.ICodeArray.CodeTooSmallError;
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

public class AbsSynTree {
	private AbsSynTreeNode root;
	
	public AbsSynTree(cs.IProgram conSynTreeRoot) {
		this.root = conSynTreeRoot.toAbstractSyntax();
	}
	
	@Override
	public String toString() {
		return root.toString("");
	}
	
	public void doScopeChecking() throws NameAlreadyDeclaredError, NameNotDeclaredError, NameAlreadyGloballyDeclaredError, LRValError, InvalidParamCountError, AlreadyInitializedError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError {
		root.saveNamespaceInfoToNode(null);
		root.doScopeChecking();
	}
	
	public void doTypeChecking() throws TypeCheckError {
		root.doTypeChecking();
	}
	
	public void doInitChecking() throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		root.doInitChecking(false);
	}
	
	public ICodeArray getCodeArray() throws CodeTooSmallError {
		root.addIInstrToCodeArray(new HashMap<String, Integer>(), false);
		// shrink the array
		AbsSynTreeNode.codeArray.resize();
		return AbsSynTreeNode.codeArray;
	}
}
