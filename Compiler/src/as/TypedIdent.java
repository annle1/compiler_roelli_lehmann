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
import token.Ident;
import token.Type;
import util.DeepCopyHelper;
import vm.ICodeArray.CodeTooSmallError;

public class TypedIdent extends AbsSynTreeNode implements Cloneable{
	private Ident ident;
	private Type type;
	private boolean isInit;
	private boolean isConst;
	private boolean needToDeref;
	
	public TypedIdent(Ident ident, Type type) {
		this.ident = ident;
		this.type = type;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	public String getValue() {
		return ident.getValue();
	}
	
	public Ident getIdent() {
		return ident;
	}
	
	public token.Type getType() {
		return type;
	}
	
	public void setInit() {
		this.isInit = true;
	}
	
	public boolean getInit() {
		return isInit;
	}
	
	public boolean getConst() {
		return isConst;
	}

	public void setConst() {
		this.isConst = true;
	}

	public void setNeedToDeref() {
		this.needToDeref = true;
	}	
	
	public boolean getNeedToDeref() {
		return needToDeref;
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
	public void doInitChecking(boolean globalProtected) throws NotInitializedError,
			AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		// Do nothing
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		// Do nothing
	}

	@Override
	public String toString(String indent) {
		String nameIndent = indent;
		String argumentIndent = indent + " ";
		String s = "";
		s += nameIndent + this.getClass().getName() + "\n";
		if(localStoresNamespace != null)
			s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream().map(Object::toString).collect(Collectors.joining(",")) + "\n";		
		s += argumentIndent + "(<ident>, <type>): (" + ident.toString() + ", " + type.toString() + ")\n";
		
		return s;
	}	
}
