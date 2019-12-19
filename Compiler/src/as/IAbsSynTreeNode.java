package as;

import java.util.ArrayList;
import java.util.HashMap;

import vm.ICodeArray;
import vm.ICodeArray.CodeTooSmallError;
import vm.IInstructions;
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

public interface IAbsSynTreeNode {
	public abstract String toString(String indent);
	public void saveNamespaceInfoToNode(HashMap<String, TypedIdent> localStoresNamespace) throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError;
	public void doScopeChecking() throws NameNotDeclaredError, LRValError, InvalidParamCountError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError;
	public void doTypeChecking() throws TypeCheckError;
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError;
	public void setInit(TypedIdent ident);
	
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly) throws CodeTooSmallError;
}
