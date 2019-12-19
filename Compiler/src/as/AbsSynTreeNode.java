package as;

import java.util.HashMap;

import vm.CodeArray;
import vm.ICodeArray;
import exception.NameAlreadyDeclaredError;

public abstract class AbsSynTreeNode implements IAbsSynTreeNode {
	static HashMap<String, TypedIdent> globalStoresNamespace = new HashMap<>();
	static HashMap<String, IDecl> globalRoutinesNamespace = new HashMap<>();
	HashMap<String, TypedIdent> localStoresNamespace = new HashMap<>();
	
	static HashMap<String, Integer> globalStoresLocation = new HashMap<>();
	static HashMap<String, Integer> globalRoutinesLocation = new HashMap<>();
	static int codeArrayPointer = 0;
	static ICodeArray codeArray = new CodeArray(1024);
	
	public void setInit(TypedIdent ident)  {
		if(localStoresNamespace.containsKey(ident.getValue()))
			localStoresNamespace.get(ident.getValue()).setInit();
	}
	
}
