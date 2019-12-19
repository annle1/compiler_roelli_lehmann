package as;

import java.util.HashMap;
import java.util.stream.Collectors;

import token.LRVal;
import token.Type;
import util.DeepCopyHelper;
import vm.IInstructions;
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

public class DebugInCmd extends AbsSynTreeNode implements ICmd {
	private IExpr expr;
	
	public DebugInCmd(IExpr expr) {
		this.expr = expr;
	}
	
	@Override
	public void saveNamespaceInfoToNode(
			HashMap<String, TypedIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		expr.saveNamespaceInfoToNode(this.localStoresNamespace);		
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValError, InvalidParamCountError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError {
		expr.doScopeChecking();
		// We need an 
		if(expr.getLRValue() != LRVal.LVAL)
			throw new LRValError(LRVal.LVAL, expr.getLRValue());
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		expr.doTypeChecking();
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		// now lets check if we try to write something into an already written constant
		// expr can only be an Init-Factor 
		InitFactor factor = (InitFactor)expr;
		// is the variable already initialized (= written once) and is a constant?
		// if yes, we are writing to an already initialized constant --> not allowed
		TypedIdent typedIdent = null;
		if(this.localStoresNamespace.containsKey(factor.ident.getValue()))
			typedIdent = this.localStoresNamespace.get(factor.ident.getValue());			
		if(globalStoresNamespace.containsKey(factor.ident.getValue())) {
			typedIdent = globalStoresNamespace.get(factor.ident.getValue());
		}
		// If this is a const and it is already initialized (once written to), throw an error
		if(typedIdent.getConst() && typedIdent.getInit())
			throw new CannotAssignToConstError(factor.ident);
		
		expr.doInitChecking(globalProtected);
		
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		InitFactor factor = (InitFactor)expr;
		// Get address
		int address;
		if(!simulateOnly) {
			if(globalStoresLocation.containsKey(factor.ident.getValue())) {
				address = globalStoresLocation.get(factor.ident.getValue());
				codeArray.put(codeArrayPointer, new IInstructions.LoadAddrAbs(address));
			} else if (localLocations.containsKey(factor.ident.getValue())) {
				address = localLocations.get(factor.ident.getValue());
				codeArray.put(codeArrayPointer, new IInstructions.LoadAddrRel(address));
			} else {
				throw new RuntimeException("WTF, no location found for variable " + factor.ident.getValue() + " ?????");
			}
		}
		codeArrayPointer++;
		if(!simulateOnly) {
			if(factor.getType() == Type.BOOL) {
				codeArray.put(codeArrayPointer, new IInstructions.InputBool(factor.ident.getValue()));
			} else if (factor.getType() == Type.INT64) {
				codeArray.put(codeArrayPointer, new IInstructions.InputInt(factor.ident.getValue()));
			} else {
				throw new RuntimeException("WTF, strange type???");
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
		s += argumentIndent + "<expr>:\n";
		s += expr.toString(subIndent);
		
		return s;
	}	
}
