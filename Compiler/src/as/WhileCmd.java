package as;

import java.util.HashMap;
import java.util.stream.Collectors;

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

public class WhileCmd extends AbsSynTreeNode implements ICmd {
	private IExpr expr;
	private CpsCmd cpsCmd;
	
	public WhileCmd(IExpr expr, CpsCmd cpsCmd) {
		this.expr = expr;
		this.cpsCmd = cpsCmd;
	}
	
	@Override
	public void saveNamespaceInfoToNode(HashMap<String, TypedIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		expr.saveNamespaceInfoToNode(this.localStoresNamespace);
		// inner while body with deepCopy from localStorage
		cpsCmd.saveNamespaceInfoToNode(DeepCopyHelper.deepCopy(this.localStoresNamespace));
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValError, InvalidParamCountError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError {
		expr.doScopeChecking();
		cpsCmd.doScopeChecking();
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		expr.doTypeChecking();
		cpsCmd.doTypeChecking();
		
		if(expr.getType() != Type.BOOL)
			throw new TypeCheckError(Type.BOOL, expr.getType());
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		expr.doInitChecking(globalProtected);
		// set recursively all initialized variables also on the child-nodes to init
		for(TypedIdent ident : localStoresNamespace.values()) {
			if(ident.getInit()) {
				cpsCmd.setInit(ident);
			}
		}
		// Do the init checking
		// Global variables cannot be initialized from now on		
		cpsCmd.doInitChecking(true);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		// get the size of cpsCmd by simulating the add action
		int codeArrayPointerBefore = codeArrayPointer;
		
		cpsCmd.addIInstrToCodeArray(localLocations, true);
		int cpsCmdSize = codeArrayPointer - codeArrayPointerBefore + 1; // + 1 for unconditional jump after exprFalse

		// reset pointer
		codeArrayPointer = codeArrayPointerBefore;
		
		// save the start of the while loop (where we save the boolean onto the stack and to the jump afterwards)
		int loopStartAddress = codeArrayPointer;
		
		// now really add the staff (NOTE: THIS IS A HEAD-CONTROLED LOOP)
		// add the boolean for the conditional check onto the stack
		expr.addIInstrToCodeArray(localLocations, simulateOnly);
		// now add the jump condition to see if we had to continue (loop part) or to jump (after the loop part)
		if(!simulateOnly)
			codeArray.put(codeArrayPointer, new IInstructions.CondJump(codeArrayPointer + 1 + cpsCmdSize));
		codeArrayPointer++;
		// now add the loop part
		cpsCmd.addIInstrToCodeArray(localLocations, simulateOnly);
		// now add the unconditional jump to jump back to the jump condition (we already processed the loop part ...)
		if(!simulateOnly)
			codeArray.put(codeArrayPointer, new IInstructions.UncondJump(loopStartAddress));
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
		s += argumentIndent + "<cpsCmd>:\n";
		s += cpsCmd.toString(subIndent);
		
		return s;
	}	
}
