package test;

import java.util.List;

import as.AbsSynTree;
import exception.AlreadyInitializedError;
import exception.CannotAssignToConstError;
import exception.CaseAlreadyDeclaredError;
import exception.DefaultCaseBoolOverkillError;
import exception.GlobalInitializationProhibitedError;
import exception.GrammarError;
import exception.InvalidParamCountError;
import exception.LRValError;
import exception.LexicalError;
import exception.NameAlreadyDeclaredError;
import exception.NameAlreadyGloballyDeclaredError;
import exception.NameNotDeclaredError;
import exception.NotInitializedError;
import exception.TypeCheckError;
import parser.Parser;
import scanner.Scanner;
import terminal.Terminal;
import token.IToken;
import token.Literal;
import token.Token;
import util.AStoCodeArrayHelper;
import util.ToubleConverter;
import vm.ICodeArray;
import vm.ICodeArray.CodeTooSmallError;
import vm.IVirtualMachine;
import vm.IVirtualMachine.ExecutionError;
import vm.VirtualMachine;

public class Test {

	public static void main(String[] args) {

		String path = "src/resources/programConditionalExpression";
		//String path = "src/resources/programSwitchCase";
		//String path = "src/resources/programSwitchCaseBool";
		//String path = "src/resources/programSwitchDiv";
		//String path = "src/resources/programFactorial";
		//String path = "src/resources/programInc";		
		
		//String path = "src/resources/programBasicDivide";
		//String path = "src/resources/programAllExceptExtension";
		//String path = "src/resources/programLeftRightAsso";
		//String path = "src/resources/programScopeChecking";
		//String path = "src/resources/programSpecialCases";
		//String path = "src/resources/programFunction";

		
		/*************************************************/

		Scanner scanner = new Scanner();
		
		/*************************************************/

		List<IToken> tokens = null;
		try {
			tokens = scanner.scan(path);
		} catch (LexicalError e) {
			System.out.println("Scanner error");
			e.printStackTrace();
		}
		
		/*************************************************/
		
		assert(tokens != null);
		
		/*************************************************/
		
		Parser parser = new Parser(tokens);
		AbsSynTree as = null;
		
		/*************************************************/
		
		try {
			as = parser.parse();
		} catch (GrammarError e) {
			System.out.println("Parser error");
			e.printStackTrace();
		} catch (NameAlreadyDeclaredError e) {
			System.out.println("Namespace check error");
			e.printStackTrace();
		} catch (NameNotDeclaredError e) {
			System.out.println("Namespace check error");
			e.printStackTrace();
		} catch (NameAlreadyGloballyDeclaredError e) {
			System.out.println("Namespace check error");
			e.printStackTrace();
		} catch (LRValError e) {
			System.out.println("LRVal check error");
			e.printStackTrace();
		} catch (InvalidParamCountError e) {
			System.out.println("Param check error");
			e.printStackTrace();
		} catch (AlreadyInitializedError e) {
			System.out.println("Init check error");
			e.printStackTrace();
		} catch (DefaultCaseBoolOverkillError e) {
			System.out.println("Switch case error");
			e.printStackTrace();
		} catch (CaseAlreadyDeclaredError e) {
			System.out.println("Switch case error");
			e.printStackTrace();
		} catch (TypeCheckError e) {
			System.out.println("Type check error");
			e.printStackTrace();
		} catch (NotInitializedError e) {
			System.out.println("Init check error");
			e.printStackTrace();
		} catch (GlobalInitializationProhibitedError e) {
			System.out.println("Init check error");
			e.printStackTrace();
		} catch (CannotAssignToConstError e) {
			System.out.println("Const error");
			e.printStackTrace();
		}
		
		/*************************************************/		
		
		assert(as != null);
		
		/*************************************************/

		
		
		AStoCodeArrayHelper asHelper = new AStoCodeArrayHelper();
		IVirtualMachine vm = null;
		try {
			System.out.println("\n******************** Generating code Array *******************");	
			ICodeArray codeArray = asHelper.convert(as);
			System.out.println(codeArray.toString());
			
			vm = new VirtualMachine(codeArray, 65536);
		} catch (ExecutionError e) {
			System.out.println("VM error");
			e.printStackTrace();
		} catch (CodeTooSmallError e) {
			System.out.println("VM error");
			e.printStackTrace();
		}		
		
		/*************************************************/		
		
		
	}
}
