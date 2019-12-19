package parser;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import as.AbsSynTree;
import cs.*;
import exception.AlreadyInitializedError;
import exception.CannotAssignToConstError;
import exception.CaseAlreadyDeclaredError;
import exception.DefaultCaseBoolOverkillError;
import exception.GlobalInitializationProhibitedError;
import exception.GrammarError;
import exception.InvalidParamCountError;
import exception.LRValError;
import exception.NameAlreadyDeclaredError;
import exception.NameAlreadyGloballyDeclaredError;
import exception.NameNotDeclaredError;
import exception.NotInitializedError;
import exception.TypeCheckError;
import terminal.Terminal;
import token.IToken;
import token.Ident;
import token.Token;
import util.ToubleConverter;

public class Parser {
	private List<IToken> tokens;
	private IToken actualToken;
	private Terminal actualTerminal;
	private int counter;
	private ToubleConverter converter;

	/******************* Callable Parser Methods **********************/

	public Parser(List<IToken> tokens) {
		this.tokens = tokens;
		this.converter = new ToubleConverter();
		nextToken();
	}

	public AbsSynTree parse() throws GrammarError, NameAlreadyDeclaredError, NameNotDeclaredError, NameAlreadyGloballyDeclaredError, LRValError, InvalidParamCountError, AlreadyInitializedError, CaseAlreadyDeclaredError, DefaultCaseBoolOverkillError, TypeCheckError, NotInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		System.out.println("\n******************** start parsing *******************");
		// start parsing with program
		IProgram program = program();
		// at the end consume the Sentinel
		consume(Terminal.SENTINEL);
		System.out.println("**************************************************");
		
		System.out.println("\n******************** Printing concrete syntax tree *******************");
		System.out.println(program.toString(""));
		System.out.println("**************************************************");
		
		System.out.println("\n******************** Printing abstract syntax tree *******************");
		as.AbsSynTree as = new as.AbsSynTree(program);
		System.out.println(as.toString());
		System.out.println("**************************************************");

		System.out.println("\n******************** Doing scope check *******************");
		as.doScopeChecking();
		System.out.println(as.toString());
		System.out.println();
		System.out.println("!!! Scope checking okay !!!");
		System.out.println("**************************************************");

		System.out.println("\n******************** Doing type check *******************");
		as.doTypeChecking();
		System.out.println();
		System.out.println("!!! Type checking okay !!!");
		System.out.println("**************************************************");

		System.out.println("\n******************** Doing init check *******************");
		as.doInitChecking();
		System.out.println();
		System.out.println("!!! Init checking okay !!!");
		System.out.println("**************************************************");			
		
		return as;
	}

	/******************* Helper Methods for the parser **********************/

	private void nextToken() {
		actualToken = tokens.get(counter++);
		actualTerminal = Enum.valueOf(Terminal.class, converter.getNameFromValue(actualToken.toString()));
	}

	private IToken consume(Terminal expectedTerminal) throws GrammarError {
		if (actualTerminal == expectedTerminal) {
			IToken consumedToken = actualToken;
			if (actualTerminal != Terminal.SENTINEL) {
				nextToken();
			}
			return consumedToken;
		} else {
			throw logGrammarException(expectedTerminal);
		}
	}

	private GrammarError logGrammarException(Terminal expectedToken) {
		String actual = converter.getNameFromValue(actualToken.toString());
		return new GrammarError("terminal expected: " + expectedToken + ", terminal found: " + actual);
	}

	/******************* Parser Methods for each NTS **********************/

	//program ::= PROGRAM IDENT globalNTS DO cpsCmd ENDPROGRAM
	private IProgram program() throws GrammarError {
		if (actualTerminal == Terminal.PROGRAM) {
			System.out.println("program ::= PROGRAM IDENT <globalNTS> DO <cpsCmd> ENDPROGRAM");
			IToken T_program = consume(Terminal.PROGRAM);
			IToken T_ident = consume(Terminal.IDENT);
			IGlobalNTS N_globalNTS = globalNTS();
			IToken T_do = consume(Terminal.DO);
			ICpsCmd N_cpsCmd = cpsCmd();
			IToken T_endprogram = consume(Terminal.ENDPROGRAM);
			return new Program(T_program, T_ident, N_globalNTS, T_do, N_cpsCmd, T_endprogram);
		} else {
			throw logGrammarException(Terminal.PROGRAM);
		}
	}

	// globalNTS ::= GLOBAL cpsDecl
	// globalNTS ::= ε
	private IGlobalNTS globalNTS() throws GrammarError {
		if (actualTerminal == Terminal.GLOBAL) {
			System.out.println("globalNTS ::= GLOBAL <cpsDecl>");
			IToken T_global = consume(Terminal.GLOBAL);
			ICpsDecl N_cpsDecl = cpsDecl();
			return new GlobalNTSGlobal(T_global, N_cpsDecl);
		} else if (actualTerminal == Terminal.DO) {
			System.out.println("globalNTS ::= ε");
			return new GlobalNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.GLOBALNTS);
		}
	}

	// cpsDecl ::= decl cpsDeclNTS
	private ICpsDecl cpsDecl() throws GrammarError {
		if (actualTerminal == Terminal.PROC || actualTerminal == Terminal.FUN || actualTerminal == Terminal.CHANGEMODE
				|| actualTerminal == Terminal.IDENT) {
			System.out.println("cpsDecl ::= <decl> <cpsDeclNTS>");
			IDecl N_decl = decl();
			ICpsDeclNTS N_cpsDeclNTS = cpsDeclNTS();
			return new CpsDecl(N_decl, N_cpsDeclNTS);
		} else {
			throw logGrammarException(Terminal.CPSDECL);
		}
	}

	// cpsDeclNTS ::= SEMICOLON decl cpsDeclNTS
	// cpsDeclNTS ::= ε
	private ICpsDeclNTS cpsDeclNTS() throws GrammarError {
		if (actualTerminal == Terminal.SEMICOLON) {
			System.out.println("cpsDeclNTS ::= SEMICOLON <decl> <cpsDeclNTS>");
			IToken T_semicolon = consume(Terminal.SEMICOLON);
			IDecl N_decl = decl();
			ICpsDeclNTS N_cpsDeclNTS = cpsDeclNTS();
			return new CpsDeclNTSSemicolon(T_semicolon, N_decl, N_cpsDeclNTS);
		} else if (actualTerminal == Terminal.DO) {
			System.out.println("cpsDeclNTS ::= ε");
			return new CpsDeclNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.CPSDECLNTS);
		}
	}
	// decl ::= stoDecl
	// decl ::= funDecl
	// decl ::= procDecl
	private IDecl decl() throws GrammarError {
		if (actualTerminal == Terminal.CHANGEMODE || actualTerminal == Terminal.IDENT) {
			System.out.println("decl ::= <stoDecl>");
			IStoDecl N_stoDecl = stoDecl();
			return new DeclSto(N_stoDecl);
		} else if (actualTerminal == Terminal.FUN) {
			System.out.println("decl ::= <funDecl>");
			IFunDecl N_funDecl = funDecl();
			return new DeclFun(N_funDecl);
		} else if (actualTerminal == Terminal.PROC) {
			System.out.println("decl ::= <procDecl>");
			IProcDecl N_procDecl = procDecl();
			return new DeclProc(N_procDecl);
		} else {
			throw logGrammarException(Terminal.DECL);
		}
	}

	// stoDecl ::= typedIdent
	// stoDecl ::= CHANGEMODE typedIdent
	private IStoDecl stoDecl() throws GrammarError {
		if (actualTerminal == Terminal.IDENT) {
			System.out.println("stoDecl ::= <typedIdent>");
			ITypedIdent N_typedIdent = typedIdent();
			return new StoDeclTypedIdent(N_typedIdent);
		} else if (actualTerminal == Terminal.CHANGEMODE) {
			System.out.println("stoDecl ::= CHANGEMODE <typedIdent>");
			IToken T_changemode = consume(Terminal.CHANGEMODE);
			ITypedIdent N_typedIdent = typedIdent();
			return new StoDeclChangemode(T_changemode, N_typedIdent);
		} else {
			throw logGrammarException(Terminal.STODECL);
		}
	}

	// typedIdent ::= IDENT COLON TYPE
	private ITypedIdent typedIdent() throws GrammarError {
		if (actualTerminal == Terminal.IDENT) {
			System.out.println("typedIdent ::= IDENT COLON TYPE");
			IToken T_ident = consume(Terminal.IDENT);
			IToken T_colon = consume(Terminal.COLON);
			IToken T_type = consume(Terminal.TYPE);
			return new TypedIdent(T_ident, T_colon, T_type);
		} else {
			throw logGrammarException(Terminal.TYPEDIDENT);
		}
	}

	// funDecl ::= FUN IDENT paramList RETURNS stoDecl funDeclNTS DO cpsCmd ENDFUN
	private IFunDecl funDecl() throws GrammarError {
		if (actualTerminal == Terminal.FUN) {
			System.out.println("funDecl ::= FUN IDENT <paramList> RETURNS <stoDecl> <funDeclNTS> DO <cpsCmd> ENDFUN");
			IToken T_fun = consume(Terminal.FUN);
			IToken T_ident = consume(Terminal.IDENT);
			IParamList N_paramList = paramList();
			IToken T_returns = consume(Terminal.RETURNS);
			IStoDecl N_stoDecl = stoDecl();
			IFunDeclNTS N_funDeclNTS = funDeclNTS();
			IToken T_do = consume(Terminal.DO);
			ICpsCmd N_cpsCmd = cpsCmd();
			IToken T_endfun = consume(Terminal.ENDFUN);
			return  new FunDecl(T_fun, T_ident, N_paramList, T_returns, N_stoDecl, N_funDeclNTS, T_do, N_cpsCmd, T_endfun);
		} else {
			throw logGrammarException(Terminal.FUNDECL);
		}
	}

	// funDeclNTS ::= LOCAL cpsStoDecl
	// funDeclNTS ::= ε
	private IFunDeclNTS funDeclNTS() throws GrammarError {
		if (actualTerminal == Terminal.LOCAL) {
			System.out.println("funDeclNTS := LOCAL <cpsStoDecl>");
			IToken T_local = consume(Terminal.LOCAL);
			ICpsStoDecl N_cpsStoDecl = cpsStoDecl();
			return new FunDeclNTSLocal(T_local, N_cpsStoDecl);
		} else if (actualTerminal == Terminal.DO) {
			System.out.println("funDeclNTS := ε");
			return new FunDeclNTSEpsilon();

		} else {
			throw logGrammarException(Terminal.FUNDECLNTS);
		}
	}

	// paramList ::= LPAREN paramListNTS RPAREN
	private IParamList paramList() throws GrammarError {
		if (actualTerminal == Terminal.LPAREN) {
			System.out.println("paramList ::= LPAREN <paramListNTS> RPAREN");
			IToken T_lparen = consume(Terminal.LPAREN);
			IParamListNTS N_paramListNTS = paramListNTS();
			IToken T_rparen = consume(Terminal.RPAREN);
			return new ParamList(T_lparen, N_paramListNTS, T_rparen);
		} else {
			throw logGrammarException(Terminal.PARAMLIST);
		}
	}

	// paramListNTS ::= param paramNTS
	// paramListNTS ::= ε	
	private IParamListNTS paramListNTS() throws GrammarError {
		if (actualTerminal == Terminal.IDENT || actualTerminal == Terminal.MECHMODE
				|| actualTerminal == Terminal.CHANGEMODE) {
			System.out.println("paramListNTS ::= <param> <paramNTS>");
			IParam N_param = param();
			IParamNTS N_paramNTS = paramNTS();
			return new ParamListNTSParam(N_param, N_paramNTS);
		} else if (actualTerminal == Terminal.RPAREN) {
			System.out.println("paramListNTS ::= ε");
			return new ParamListNTSEpsilon();

		} else {
			throw logGrammarException(Terminal.PARAMLISTNTS);
		}
	}

	// paramNTS ::= COMMA param paramNTS
	// paramNTS ::= ε	
	private IParamNTS paramNTS() throws GrammarError {
		if (actualTerminal == Terminal.COMMA) {
			System.out.println("paramNTS ::= COMMA <param> <paramNTS>");
			IToken T_comma = consume(Terminal.COMMA);
			IParam N_param = param();
			IParamNTS N_paramNTS = paramNTS();
			return new ParamNTSComma(T_comma, N_param, N_paramNTS);
		} else if (actualTerminal == Terminal.RPAREN) {
			System.out.println("paramNTS ::= ε");
			return new ParamNTSEpsilon();

		} else {
			throw logGrammarException(Terminal.PARAMNTS);
		}
	}

	// param ::= mechModeNTS changeModeNTS typedIdent
	private IParam param() throws GrammarError {
		if (actualTerminal == Terminal.IDENT || actualTerminal == Terminal.CHANGEMODE
				|| actualTerminal == Terminal.MECHMODE) {
			System.out.println("param ::= <mechModeNTS> <changeModeNTS> <typedIdent>");
			IMechModeNTS N_mechModeNTS = mechModeNTS();
			IChangeModeNTS N_changeModeNTS = changeModeNTS();
			ITypedIdent N_typedIdent = typedIdent();
			return new Param(N_mechModeNTS, N_changeModeNTS, N_typedIdent);
		} else {
			throw logGrammarException(Terminal.PARAM);
		}
	}

	// changeModeNTS ::= CHANGEMODE
	// changeModeNTS::= ε
	private IChangeModeNTS changeModeNTS() throws GrammarError {
		if (actualTerminal == Terminal.CHANGEMODE) {
			System.out.println("changeModeNTS ::= CHANGEMODE");
			IToken T_changemode = consume(Terminal.CHANGEMODE);
			return new ChangeModeNTSChangeMode(T_changemode);
		} else if (actualTerminal == Terminal.IDENT 
				|| actualTerminal == Terminal.MECHMODE) {
			System.out.println("changeModeNTS ::= ε");
			return new ChangeModeNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.CHANGEMODENTS);
		}
	}

	// mechModeNTS ::= MECHMODE
	// mechModeNTS::= ε	
	private IMechModeNTS mechModeNTS() throws GrammarError {
		if (actualTerminal == Terminal.MECHMODE) {
			System.out.println("mechModeNTS ::= MECHMODE");
			IToken T_mechmode = consume(Terminal.MECHMODE);
			return new MechModeNTSMechMode(T_mechmode);
		} else if (actualTerminal == Terminal.IDENT) {
			System.out.println("mechModeNTS ::= ε");
			return new MechModeNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.MECHMODENTS);
		}
	}

	// cpsCmd ::= cmd cpsCmdNTS
	private ICpsCmd cpsCmd() throws GrammarError {
		if (actualTerminal == Terminal.DEBUGOUT || actualTerminal == Terminal.DEBUGIN || actualTerminal == Terminal.CALL
				|| actualTerminal == Terminal.SWITCH || actualTerminal == Terminal.WHILE
				|| actualTerminal == Terminal.IF || actualTerminal == Terminal.LPAREN
				|| actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT
				|| actualTerminal == Terminal.IDENT || actualTerminal == Terminal.LITERAL
				|| actualTerminal == Terminal.SKIP) {
			System.out.println("cpsCMD ::= <cmd> <cpsCmdNTS>");
			ICmd N_cmd = cmd();
			ICpsCmdNTS N_cpsCmdNTS = cpsCmdNTS();
			return new CpsCmd(N_cmd, N_cpsCmdNTS);
		} else {
			throw logGrammarException(Terminal.CPSCMD);
		}
	}

	// cpsCmdNTS ::= SEMICOLON cmd cpsCmdNTS
	// cpsCmdNTS ::= ε	
	private ICpsCmdNTS cpsCmdNTS() throws GrammarError {
		if (actualTerminal == Terminal.SEMICOLON) {
			System.out.println("cpsCmdNTS ::= SEMICOLON <cmd> <cpsCmdNTS>");
			IToken T_semicolon = consume(Terminal.SEMICOLON);
			ICmd N_cmd = cmd();
			ICpsCmdNTS N_cpsCmdNTS = cpsCmdNTS();
			return new CpsCmdNTSSemicolon(T_semicolon, N_cmd, N_cpsCmdNTS);
		} else if (actualTerminal == Terminal.ENDPROC || actualTerminal == Terminal.ENDCASE
				|| actualTerminal == Terminal.ENDWHILE || actualTerminal == Terminal.ENDIF
				|| actualTerminal == Terminal.ELSE || actualTerminal == Terminal.ENDFUN
				|| actualTerminal == Terminal.ENDPROGRAM) {
			System.out.println("cpsCmdNTS ::= ε");
			return new CpsCmdNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.CPSCMDNTS);
		}
	}

	// cmd ::= SKIP
	// cmd ::= expr BECOMES expr
	// cmd ::= IF expr THEN cpsCmd ifelseNTS ENDIF
	// cmd ::= WHILE expr DO cpsCmd ENDWHILE	
	// cmd ::= SWITCH expr caseNTS defaultCaseNTS ENDSWITCH		
	// cmd ::= CALL IDENT exprList
	// cmd ::= DEBUGIN expr
	// cmd ::= DEBUGOUT expr						
	private ICmd cmd() throws GrammarError {
		if (actualTerminal == Terminal.SKIP) {
			System.out.println("cmd ::= SKIP");
			IToken T_skip = consume(Terminal.SKIP);
			return new CmdSkip(T_skip);
		} else
			if (actualTerminal == Terminal.LPAREN || actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT
					|| actualTerminal == Terminal.IDENT || actualTerminal == Terminal.LITERAL) {
			System.out.println("cmd ::= <expr> BECOMES <expr>");
			IExpr N_expr1 = expr();
			IToken T_becomes = consume(Terminal.BECOMES);
			IExpr N_expr2 = expr();
			return new CmdExpr(N_expr1, T_becomes, N_expr2);
		} else if (actualTerminal == Terminal.IF) {
			System.out.println("cmd ::= IF <expr> THEN <cpsCmd> <ifelseNTS> ENDIF");
			IToken T_if = consume(Terminal.IF);
			IExpr N_expr = expr();
			IToken T_then = consume(Terminal.THEN);
			ICpsCmd N_cpsCmd = cpsCmd();
			IIfelseNTS N_ifelseNTS = ifelseNTS();
			IToken T_endif = consume(Terminal.ENDIF);
			return new CmdIf(T_if, N_expr, T_then, N_cpsCmd, N_ifelseNTS, T_endif);
		} else if (actualTerminal == Terminal.WHILE) {
			System.out.println("cmd ::= WHILE <expr> DO <cpsCmd> ENDWHILE");
			IToken T_while = consume(Terminal.WHILE);
			IExpr N_expr = expr();
			IToken T_do = consume(Terminal.DO);
			ICpsCmd N_cpsCmd = cpsCmd();
			IToken T_endwhile = consume(Terminal.ENDWHILE);
			return new CmdWhile(T_while, N_expr, T_do, N_cpsCmd, T_endwhile);
		} else if (actualTerminal == Terminal.SWITCH) {
			System.out.println("cmd ::= SWITCH <expr> <caseNTS> <defaultCaseNTS> ENDSWITCH");
			IToken T_switch = consume(Terminal.SWITCH);
			IExpr N_expr = expr();
			ICaseNTS N_caseNTS = caseNTS();
			IDefaultCaseNTS N_defaultCaseNTS = defaultCaseNTS();
			IToken T_endswitch = consume(Terminal.ENDSWITCH);
			return new CmdSwitch(T_switch, N_expr, N_caseNTS, N_defaultCaseNTS, T_endswitch);
		} else if (actualTerminal == Terminal.CALL) {
			System.out.println("cmd ::= CALL IDENT <exprList>");
			IToken T_call = consume(Terminal.CALL);
			IToken T_ident = consume(Terminal.IDENT);
			IExprList N_exprList = exprList();
			return new CmdCall(T_call, T_ident, N_exprList);
		} else if (actualTerminal == Terminal.DEBUGIN) {
			System.out.println("cmd ::= DEBUGIN <expr>");
			IToken T_debugin = consume(Terminal.DEBUGIN);
			IExpr N_expr = expr();
			return new CmdDebugin(T_debugin, N_expr);
		} else if (actualTerminal == Terminal.DEBUGOUT) {
			System.out.println("cmd ::= DEBUGOUT <expr>");
			IToken T_debugout = consume(Terminal.DEBUGOUT);
			IExpr N_expr = expr();
			return new CmdDebugout(T_debugout, N_expr);
		} else {
			throw logGrammarException(Terminal.CMD);
		}
	}

	// ifelseNTS ::= ELSE cpsCmd
	// ifelseNTS ::= ε 	
	private IIfelseNTS ifelseNTS() throws GrammarError {
		if (actualTerminal == Terminal.ELSE) {
			System.out.println("ifelseNTS ::= ELSE <cpsCmd>");
			IToken T_else = consume(Terminal.ELSE);
			ICpsCmd N_cpsCmd = cpsCmd();
			return new IfelseNTSElse(T_else, N_cpsCmd);
		} else if (actualTerminal == Terminal.ENDIF) {
			System.out.println("ifelseNTS ::= ε");
			return new IfelseNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.IFELSENTS);
		}
	}

	// caseNTS::= CASE LITERAL COLON cpsCmd ENDCASE caseNTS
	// caseNTS::= ε	
	private ICaseNTS caseNTS() throws GrammarError {
		if (actualTerminal == Terminal.CASE) {
			System.out.println("caseNTS ::= CASE LITERAL COLON <cpsCmd> ENDCASE <caseNTS>");
			IToken T_case = consume(Terminal.CASE);
			IToken T_literal = consume(Terminal.LITERAL);
			IToken T_colon = consume(Terminal.COLON);
			ICpsCmd N_cpsCmd = cpsCmd();
			IToken T_endcase = consume(Terminal.ENDCASE);
			ICaseNTS N_caseNTS = caseNTS();
			return new CaseNTSCase(T_case, T_literal, T_colon, N_cpsCmd, T_endcase, N_caseNTS);
		} else if (actualTerminal == Terminal.ENDSWITCH || actualTerminal == Terminal.DEFAULTCASE) {
			System.out.println("caseNTS ::= ε");
			return new CaseNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.CASENTS);
		}
	}

	// defaultCaseNTS ::= DEFAULTCASE COLON cpsCmd ENDCASE
	// defaultCaseNTS ::= ε	
	private IDefaultCaseNTS defaultCaseNTS() throws GrammarError {
		if (actualTerminal == Terminal.DEFAULTCASE) {
			System.out.println("defaultCaseNTS ::= DEFAULTCASE COLON <cpsCmd> ENDCASE");
			IToken T_defaultcase = consume(Terminal.DEFAULTCASE);
			IToken T_colon = consume(Terminal.COLON);
			ICpsCmd N_cpsCmd = cpsCmd();
			IToken T_endcase = consume(Terminal.ENDCASE);
			return new DefaultCaseNTSDefaultcase(T_defaultcase, T_colon, N_cpsCmd, T_endcase);
		} else if (actualTerminal == Terminal.ENDSWITCH) {
			System.out.println("defaultCaseNTS ::= ε");
			return new DefaultCaseNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.DEFAULTCASENTS);
		}
	}

	// expr ::= term0 condExprNTS
	private IExpr expr() throws GrammarError {
		if (actualTerminal == Terminal.LPAREN || actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT
				|| actualTerminal == Terminal.IDENT || actualTerminal == Terminal.LITERAL) {
			System.out.println("expr ::= <term0> <condExprNTS>");
			ITerm0 N_term0 = term0();
			ICondExprNTS N_condExprNTS = condExprNTS();
			return new Expr(N_term0, N_condExprNTS);
		} else {
			throw logGrammarException(Terminal.EXPR);
		}
	}

	// condExprNTS ::= QUESTIONMARK expr COLON expr
	// condExprNTS ::= ε 	
	private ICondExprNTS condExprNTS() throws GrammarError {
		if (actualTerminal == Terminal.QUESTIONMARK) {
			System.out.println("condExprNTS ::= QUESTIONMARK <expr> COLON <expr>");
			IToken T_questionmark = consume(Terminal.QUESTIONMARK);
			IExpr N_expr1 = expr();
			IToken T_colon = consume(Terminal.COLON);
			IExpr N_expr2 = expr();
			return new CondExprNTSQuestionmark(T_questionmark, N_expr1, T_colon, N_expr2);
		} else if (actualTerminal == Terminal.COMMA || actualTerminal == Terminal.RPAREN
				|| actualTerminal == Terminal.COLON || actualTerminal == Terminal.ENDSWITCH
				|| actualTerminal == Terminal.DEFAULTCASE || actualTerminal == Terminal.CASE
				|| actualTerminal == Terminal.DO || actualTerminal == Terminal.THEN
				|| actualTerminal == Terminal.ENDPROC || actualTerminal == Terminal.ENDCASE
				|| actualTerminal == Terminal.ENDWHILE || actualTerminal == Terminal.ENDIF
				|| actualTerminal == Terminal.ELSE || actualTerminal == Terminal.ENDFUN
				|| actualTerminal == Terminal.ENDPROGRAM || actualTerminal == Terminal.SEMICOLON
				|| actualTerminal == Terminal.BECOMES) {
			System.out.println("condExprNTS ::= ε");
			return new CondExprNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.CONDEXPRNTS);
		}
	}

	// term0 ::= term1 term0NTS
	private ITerm0 term0() throws GrammarError {
		if (actualTerminal == Terminal.LPAREN || actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT
				|| actualTerminal == Terminal.IDENT || actualTerminal == Terminal.LITERAL) {
			System.out.println("term0 ::= <term1> <term0NTS>");
			ITerm1 N_term1 = term1();
			ITerm0NTS N_term0NTS = term0NTS();
			return new Term0(N_term1, N_term0NTS);
		} else {
			throw logGrammarException(Terminal.TERM0);
		}
	}

	// term0NTS ::= BOOLOPR term1 term0NTS
	// term0NTS ::= ε	
	private ITerm0NTS term0NTS() throws GrammarError {
		if (actualTerminal == Terminal.BOOLOPR) {
			System.out.println("term0NTS ::= BOOLOPR <term1> <term0NTS>");
			IToken T_boolopr = consume(Terminal.BOOLOPR);
			ITerm1 N_term1 = term1();
			ITerm0NTS N_term0NTS = term0NTS();
			return new Term0NTSBoolopr(T_boolopr, N_term1, N_term0NTS);
		} else if (actualTerminal == Terminal.COMMA || actualTerminal == Terminal.RPAREN
				|| actualTerminal == Terminal.COLON || actualTerminal == Terminal.ENDSWITCH
				|| actualTerminal == Terminal.DEFAULTCASE || actualTerminal == Terminal.CASE
				|| actualTerminal == Terminal.DO || actualTerminal == Terminal.THEN
				|| actualTerminal == Terminal.ENDPROC || actualTerminal == Terminal.ENDCASE
				|| actualTerminal == Terminal.ENDWHILE || actualTerminal == Terminal.ENDIF
				|| actualTerminal == Terminal.ELSE || actualTerminal == Terminal.ENDFUN
				|| actualTerminal == Terminal.ENDPROGRAM || actualTerminal == Terminal.SEMICOLON
				|| actualTerminal == Terminal.BECOMES || actualTerminal == Terminal.QUESTIONMARK) {
			System.out.println("term0NTS ::= ε");
			return new Term0NTSEpsilon();
		} else {
			throw logGrammarException(Terminal.TERM0NTS);
		}
	}

	// term1 ::= term2 term1NTS
	private ITerm1 term1() throws GrammarError {
		if (actualTerminal == Terminal.LPAREN || actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT
				|| actualTerminal == Terminal.IDENT || actualTerminal == Terminal.LITERAL) {
			System.out.println("term1 ::= <term2> <term1NTS>");
			ITerm2 N_term2 = term2();
			ITerm1NTS N_term1NTS = term1NTS();
			return new Term1(N_term2, N_term1NTS);
		} else {
			throw logGrammarException(Terminal.TERM1);
		}
	}

	// term1NTS::= RELOPR term2
	// term1NTS::= ε	
	private ITerm1NTS term1NTS() throws GrammarError {
		if (actualTerminal == Terminal.RELOPR) {
			System.out.println("term1NTS ::= RELOPR <term2>");
			IToken T_relopr = consume(Terminal.RELOPR);
			ITerm2 N_term2 = term2();
			return new Term1NTSRelopr(T_relopr, N_term2);
		} else if (actualTerminal == Terminal.COMMA || actualTerminal == Terminal.RPAREN
				|| actualTerminal == Terminal.COLON || actualTerminal == Terminal.ENDSWITCH
				|| actualTerminal == Terminal.DEFAULTCASE || actualTerminal == Terminal.CASE
				|| actualTerminal == Terminal.DO || actualTerminal == Terminal.THEN
				|| actualTerminal == Terminal.ENDPROC || actualTerminal == Terminal.ENDCASE
				|| actualTerminal == Terminal.ENDWHILE || actualTerminal == Terminal.ENDIF
				|| actualTerminal == Terminal.ELSE || actualTerminal == Terminal.ENDFUN
				|| actualTerminal == Terminal.ENDPROGRAM || actualTerminal == Terminal.SEMICOLON
				|| actualTerminal == Terminal.BECOMES || actualTerminal == Terminal.QUESTIONMARK
				|| actualTerminal == Terminal.BOOLOPR) {
			System.out.println("term1NTS ::= ε");
			return new Term1NTSEpsilon();
		} else {
			throw logGrammarException(Terminal.TERM1NTS);
		}
	}

	// term2 ::= term3 term2NTS
	private ITerm2 term2() throws GrammarError {
		if (actualTerminal == Terminal.LPAREN || actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT
				|| actualTerminal == Terminal.IDENT || actualTerminal == Terminal.LITERAL) {
			System.out.println("term2 ::= <term3> <term2NTS>");
			ITerm3 N_term3 = term3();
			ITerm2NTS N_term2NTS = term2NTS();
			return new Term2(N_term3, N_term2NTS);
		} else {
			throw logGrammarException(Terminal.TERM2);
		}
	}

	// term2NTS ::= ADDOPR term3 term2NTS
	// term2NTS ::= ε	
	private ITerm2NTS term2NTS() throws GrammarError {
		if (actualTerminal == Terminal.ADDOPR) {
			System.out.println("term2NTS ::= ADDOPR <term3> <term2NTS>");
			IToken T_addopr = consume(Terminal.ADDOPR);
			ITerm3 N_term3 = term3();
			ITerm2NTS N_term2NTS = term2NTS();
			return new Term2NTSAddopr(T_addopr, N_term3, N_term2NTS);
		} else if (actualTerminal == Terminal.COMMA || actualTerminal == Terminal.RPAREN
				|| actualTerminal == Terminal.COLON || actualTerminal == Terminal.ENDSWITCH
				|| actualTerminal == Terminal.DEFAULTCASE || actualTerminal == Terminal.CASE
				|| actualTerminal == Terminal.DO || actualTerminal == Terminal.THEN
				|| actualTerminal == Terminal.ENDPROC || actualTerminal == Terminal.ENDCASE
				|| actualTerminal == Terminal.ENDWHILE || actualTerminal == Terminal.ENDIF
				|| actualTerminal == Terminal.ELSE || actualTerminal == Terminal.ENDFUN
				|| actualTerminal == Terminal.ENDPROGRAM || actualTerminal == Terminal.SEMICOLON
				|| actualTerminal == Terminal.BECOMES || actualTerminal == Terminal.QUESTIONMARK
				|| actualTerminal == Terminal.BOOLOPR || actualTerminal == Terminal.RELOPR) {
			System.out.println("term2NTS ::= ε");
			return new Term2NTSEpsilon();
		} else {
			throw logGrammarException(Terminal.TERM2NTS);
		}
	}

	// term3 ::= factor term3NTS
	private ITerm3 term3() throws GrammarError {
		if (actualTerminal == Terminal.LPAREN || actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT
				|| actualTerminal == Terminal.IDENT || actualTerminal == Terminal.LITERAL) {
			System.out.println("term3 ::= <factor> <term3NTS>");
			IFactor N_factor = factor();
			ITerm3NTS N_term3NTS = term3NTS();
			return new Term3(N_factor, N_term3NTS);
		} else {
			throw logGrammarException(Terminal.TERM3);
		}
	}

	// term3NTS ::= MULTOPR factor term3NTS
	// term3NTS ::= ε	
	private ITerm3NTS term3NTS() throws GrammarError {
		if (actualTerminal == Terminal.MULTOPR) {
			System.out.println("term3NTS ::= MULTOPR <factor> <term3NTS>");
			IToken T_multopr = consume(Terminal.MULTOPR);
			IFactor N_factor = factor();
			ITerm3NTS N_term3NTS = term3NTS();
			return new Term3NTSMultopr(T_multopr, N_factor, N_term3NTS);
		} else if (actualTerminal == Terminal.COMMA || actualTerminal == Terminal.RPAREN
				|| actualTerminal == Terminal.COLON || actualTerminal == Terminal.ENDSWITCH
				|| actualTerminal == Terminal.DEFAULTCASE || actualTerminal == Terminal.CASE
				|| actualTerminal == Terminal.DO || actualTerminal == Terminal.THEN
				|| actualTerminal == Terminal.ENDPROC || actualTerminal == Terminal.ENDCASE
				|| actualTerminal == Terminal.ENDWHILE || actualTerminal == Terminal.ENDIF
				|| actualTerminal == Terminal.ELSE || actualTerminal == Terminal.ENDFUN
				|| actualTerminal == Terminal.ENDPROGRAM || actualTerminal == Terminal.SEMICOLON
				|| actualTerminal == Terminal.BECOMES || actualTerminal == Terminal.QUESTIONMARK
				|| actualTerminal == Terminal.BOOLOPR || actualTerminal == Terminal.RELOPR
				|| actualTerminal == Terminal.ADDOPR) {
			System.out.println("term3NTS ::= ε");
			return new Term3NTSEpsilon();
		} else {
			throw logGrammarException(Terminal.TERM3NTS);
		}
	}

	// factor ::= LITERAL
	// factor ::= IDENT factorNTS	
	// factor ::= monadicOpr factor
	// factor ::= LPAREN expr RPAREN	
	private IFactor factor() throws GrammarError {
		if (actualTerminal == Terminal.LITERAL) {
			System.out.println("factor ::= LITERAL");
			IToken T_literal = consume(Terminal.LITERAL);
			return new FactorLiteral(T_literal);
		} else if (actualTerminal == Terminal.IDENT) {
			System.out.println("factor ::= IDENT <factorNTS>");
			IToken T_ident = consume(Terminal.IDENT);
			IFactorNTS N_factorNTS = factorNTS();
			return new FactorIdent(T_ident, N_factorNTS);
		} else if (actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT) {
			System.out.println("factor ::= <monadicOpr> <factor>");
			IMonadicOpr N_monadicOpr = monadicOpr();
			IFactor N_factor = factor();
			return new FactorMonadicopr(N_monadicOpr, N_factor);
		} else if (actualTerminal == Terminal.LPAREN) {
			System.out.println("factor ::= LPAREN <expr> RPAREN");
			IToken T_lparen = consume(Terminal.LPAREN);
			IExpr N_expr = expr();
			IToken T_rparen = consume(Terminal.RPAREN);
			return new FactorLParen(T_lparen, N_expr, T_rparen);
		} else {
			throw logGrammarException(Terminal.FACTOR);
		}
	}

	// factorNTS ::= INIT
	// factorNTS ::= exprList
	// factorNTS ::= ε	
	private IFactorNTS factorNTS() throws GrammarError {
		if (actualTerminal == Terminal.INIT) {
			System.out.println("factorNTS ::= INIT");
			IToken T_init = consume(Terminal.INIT);
			return new FactorNTSInit(T_init);
		} else if (actualTerminal == Terminal.LPAREN) {
			System.out.println("factorNTS ::= <exprList>");
			IExprList N_exprList = exprList();
			return new FactorNTSExprList(N_exprList);
		} else if (actualTerminal == Terminal.COMMA || actualTerminal == Terminal.RPAREN
				|| actualTerminal == Terminal.COLON || actualTerminal == Terminal.ENDSWITCH
				|| actualTerminal == Terminal.DEFAULTCASE || actualTerminal == Terminal.CASE
				|| actualTerminal == Terminal.DO || actualTerminal == Terminal.THEN
				|| actualTerminal == Terminal.ENDPROC || actualTerminal == Terminal.ENDCASE
				|| actualTerminal == Terminal.ENDWHILE || actualTerminal == Terminal.ENDIF
				|| actualTerminal == Terminal.ELSE || actualTerminal == Terminal.ENDFUN
				|| actualTerminal == Terminal.ENDPROGRAM || actualTerminal == Terminal.SEMICOLON
				|| actualTerminal == Terminal.BECOMES || actualTerminal == Terminal.QUESTIONMARK
				|| actualTerminal == Terminal.BOOLOPR || actualTerminal == Terminal.RELOPR
				|| actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.MULTOPR) {
			System.out.println("factorNTS ::= ε");
			return new FactorNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.FACTORNTS);
		}
	}

	// exprList ::= LPAREN exprListLparenNTS RPAREN
	private IExprList exprList() throws GrammarError {
		if (actualTerminal == Terminal.LPAREN) {
			System.out.println("exprList ::= LPAREN <exprListLparenNTS> RPAREN");
			IToken T_lparen = consume(Terminal.LPAREN);
			IExprListLparenNTS N_exprListLparenNTS = exprListLparenNTS();
			IToken T_rparen = consume(Terminal.RPAREN);
			return new ExprList(T_lparen, N_exprListLparenNTS, T_rparen);
		} else {
			throw logGrammarException(Terminal.EXPRLIST);
		}
	}

	// exprListLparenNTS ::= expr exprListNTS
	// exprListLparenNTS ::= ε	
	private IExprListLparenNTS exprListLparenNTS() throws GrammarError {
		if (actualTerminal == Terminal.LPAREN || actualTerminal == Terminal.ADDOPR || actualTerminal == Terminal.NOT
				|| actualTerminal == Terminal.IDENT || actualTerminal == Terminal.LITERAL) {
			System.out.println("exprListLparenNTS ::= <expr> <exprListNTS>");
			IExpr N_expr = expr();
			IExprListNTS N_exprListNTS = exprListNTS();
			return new ExprListLparenNTSExpr(N_expr, N_exprListNTS);
		} else if (actualTerminal == Terminal.RPAREN) {
			System.out.println("exprListLparenNTS ::= ε");
			return new ExprListLparenNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.EXPRLISTLPARENNTS);
		}
	}

	// exprListNTS ::= COMMA expr exprListNTS
	// exprListNTS ::= ε	
	private IExprListNTS exprListNTS() throws GrammarError {
		if (actualTerminal == Terminal.COMMA) {
			System.out.println("exprListNTS ::= COMMA <expr> <exprListNTS>");
			IToken T_comma = consume(Terminal.COMMA);
			IExpr N_expr = expr();
			IExprListNTS N_exprListNTS = exprListNTS();
			return new ExprListNTSComma(T_comma, N_expr, N_exprListNTS);
		} else if (actualTerminal == Terminal.RPAREN) {
			System.out.println("exprListNTS ::= ε");
			return new ExprListNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.EXPRLISTNTS);
		}
	}

	// monadicOpr ::= NOT
	// monadicOpr ::= ADDOPR	
	private IMonadicOpr monadicOpr() throws GrammarError {
		if (actualTerminal == Terminal.NOT) {
			System.out.println("monadicOpr ::= NOT");
			IToken T_not = consume(Terminal.NOT);
			return new MonadicOprNot(T_not);
		} else if (actualTerminal == Terminal.ADDOPR) {
			System.out.println("monadicOpr ::= ADDOPR");
			IToken T_addopr = consume(Terminal.ADDOPR);
			return new MonadicOprAddopr(T_addopr);
		} else {
			throw logGrammarException(Terminal.MONADICOPR);
		}
	}

	// cpsStoDecl ::= stoDecl cpsStoDeclNTS
	private ICpsStoDecl cpsStoDecl() throws GrammarError {
		if (actualTerminal == Terminal.CHANGEMODE || actualTerminal == Terminal.IDENT) {
			System.out.println("cpsStoDecl ::= <stoDecl> <cpsStoDeclNTS>");
			IStoDecl N_stoDecl = stoDecl();
			ICpsStoDeclNTS N_cpsStoDeclNTS = cpsStoDeclNTS();
			return new CpsStoDecl(N_stoDecl, N_cpsStoDeclNTS);
		} else {
			throw logGrammarException(Terminal.CPSSTODECL);
		}
	}

	// cpsStoDeclNTS ::= SEMICOLON stoDecl cpsStoDeclNTS
	// cpsStoDeclNTS ::= ε	
	private ICpsStoDeclNTS cpsStoDeclNTS() throws GrammarError {
		if (actualTerminal == Terminal.SEMICOLON) {
			System.out.println("cpsStoDeclNTS ::= SEMICOLON <stoDecl> <cpsStoDeclNTS>");
			IToken T_semicolon = consume(Terminal.SEMICOLON);
			IStoDecl N_stoDecl = stoDecl();
			ICpsStoDeclNTS N_cpsStoDeclNTS = cpsStoDeclNTS();
			return new CpsStoDeclNTSSemicolon(T_semicolon, N_stoDecl, N_cpsStoDeclNTS);
		} else if (actualTerminal == Terminal.DO) {
			System.out.println("cpsStoDeclNTS ::= ε");
			return new CpsStoDeclNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.CPSSTODECLNTS);
		}
	}

	// procDecl ::= PROC IDENT paramList procDeclNTS DO cpsCmd ENDPROC
	private IProcDecl procDecl() throws GrammarError {
		if (actualTerminal == Terminal.PROC) {
			System.out.println("procDecl ::= PROC IDENT <paramList> <procDeclNTS> DO <cpsCmd> ENDPROC");
			IToken T_proc = consume(Terminal.PROC);
			IToken T_ident = consume(Terminal.IDENT);
			IParamList N_paramList = paramList();
			IProcDeclNTS N_procDeclNTS = procDeclNTS();
			IToken T_do = consume(Terminal.DO);
			ICpsCmd N_cpsCmd = cpsCmd();
			IToken T_endproc = consume(Terminal.ENDPROC);
			return new ProcDecl(T_proc, T_ident, N_paramList, N_procDeclNTS, T_do, N_cpsCmd, T_endproc);
		} else {
			throw logGrammarException(Terminal.PROCDECL);
		}
	}

	// procDeclNTS ::= LOCAL cpsStoDecl
	// procDeclNTS ::= ε	
	private IProcDeclNTS procDeclNTS() throws GrammarError {
		if (actualTerminal == Terminal.LOCAL) {
			System.out.println("procDeclNTS ::= LOCAL <cpsStoDecl>");
			IToken T_local = consume(Terminal.LOCAL);
			ICpsStoDecl N_cpsStoDecl = cpsStoDecl();
			return new ProcDeclNTSLocal(T_local, N_cpsStoDecl);
		} else if (actualTerminal == Terminal.DO) {
			System.out.println("procDeclNTS ::= ε");
			return new ProcDeclNTSEpsilon();
		} else {
			throw logGrammarException(Terminal.PROCDECLNTS);
		}
	}
}