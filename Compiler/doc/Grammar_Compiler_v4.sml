datatype term = 
	  PROGRAM 
	| IDENT 
	| DO 
	| ENDPROGRAM
	| GLOBAL 
	| SEMICOLON 
	| CHANGEMODE 
	| COLON 
	| TYPE
	| FUN 
	| RETURNS 
	| ENDFUN
	| LOCAL 
	| LPAREN 
	| RPAREN
	| COMMA 
	| MECHMODE 
	| SKIP
	| BECOMES 
	| IF 
	| THEN 
	| ENDIF
	| ELSE 
	| WHILE 
	| ENDWHILE
	| SWITCH 
	| CASE 
	| LITERAL 
	| ENDCASE 
	| ENDSWITCH
	| DEFAULTCASE 
	| DEBUGIN 
	| DEBUGOUT 
	| CALL 
	| QUESTIONMARK 
	| BOOLOPR 
	| RELOPR 
	| ADDOPR 
	| MULTOPR 
	| INIT
	| NOT
	| PROC 
	| ENDPROC

val string_of_term    =
   fn PROGRAM 		  => "PROGRAM"
	| IDENT           => "IDENT"
	| DO              => "DO"
	| ENDPROGRAM      => "ENDPROGRAM"
	| GLOBAL          => "GLOBAL"
	| SEMICOLON       => "SEMICOLON"
	| CHANGEMODE      => "CHANGEMODE"
	| COLON           => "COLON"
	| TYPE            => "TYPE"
	| FUN             => "FUN"
	| RETURNS         => "RETURNS"
	| ENDFUN          => "ENDFUN"
	| LOCAL           => "LOCAL"
	| LPAREN          => "LPAREN"
	| RPAREN          => "RPAREN"
	| COMMA           => "COMMA"
	| MECHMODE        => "MECHMODE"
	| SKIP            => "SKIP"
	| BECOMES         => "BECOMES"
	| IF              => "IF"
	| THEN            => "THEN"
	| ENDIF           => "ENDIF"
	| ELSE            => "ELSE"
	| WHILE           => "WHILE"
	| ENDWHILE        => "ENDWHILE"
	| SWITCH          => "SWITCH"
	| CASE            => "CASE"
	| LITERAL         => "LITERAL"
	| ENDCASE         => "ENDCASE"
	| ENDSWITCH       => "ENDSWITCH"
	| DEFAULTCASE     => "DEFAULTCASE"
	| DEBUGIN         => "DEBUGIN"
	| DEBUGOUT        => "DEBUGOUT"
	| CALL            => "CALL"
	| QUESTIONMARK    => "QUESTIONMARK"
	| BOOLOPR         => "BOOLOPR"
	| RELOPR          => "RELOPR"
	| ADDOPR          => "ADDOPR"
	| MULTOPR         => "MULTOPR"
	| INIT            => "INIT"
	| NOT             => "NOT"
	| PROC            => "PROC"
	| ENDPROC         => "ENDPROC"

datatype nonterm = 
	  program 
	| globalNTS 
	| cpsDeclNTS 
	| cpsDecl 
	| decl 
	| stoDecl 
	| typedIdent 
	| funDecl 
	| funDeclNTS
	| paramList
	| paramListNTS
	| param 	
	| paramNTS
	| changeModeNTS
	| mechModeNTS 
	| cpsCmd 
	| cpsCmdNTS 
	| cmd 
	| ifelseNTS 
	| caseNTS
	| defaultCaseNTS 
	| expr 
	| condExprNTS 
	| term0
	| term0NTS
	| term1
	| term1NTS
	| term2 
	| term2NTS 
	| term3 
	| term3NTS 
	| factor 
	| factorNTS
	| exprList 
	| exprListLparenNTS
	| exprListNTS 
	| monadicOpr 
	| cpsStoDecl 
	| cpsStoDeclNTS 
	| procDecl
	| procDeclNTS

val string_of_nonterm    =
	fn  program 		 => "program"
	| globalNTS          => "globalNTS"
	| cpsDeclNTS         => "cpsDeclNTS"
	| cpsDecl            => "cpsDecl"
	| decl               => "decl"
	| stoDecl            => "stoDecl"
	| typedIdent         => "typedIdent"
	| funDecl            => "funDecl"
	| funDeclNTS         => "funDeclNTS"
	| paramList          => "paramList"
	| paramListNTS       => "paramListNTS"
	| paramNTS           => "paramNTS"
	| param              => "param"
	| changeModeNTS		 => "changeModeNTS"
	| mechModeNTS 		 => "mechModeNTS"
	| cpsCmd             => "cpsCmd"
	| cpsCmdNTS          => "cpsCmdNTS"
	| cmd                => "cmd"
	| ifelseNTS          => "ifelseNTS"
	| caseNTS            => "caseNTS"
	| defaultCaseNTS     => "defaultCaseNTS"
	| expr               => "expr"
	| condExprNTS        => "condExprNTS"
	| term0              => "term0"
	| term0NTS           => "term0NTS"
	| term1              => "term1"
	| term1NTS           => "term1NTS"
	| term2              => "term2"
	| term2NTS           => "term2NTS"
	| term3              => "term3"
	| term3NTS           => "term3NTS"
	| factor             => "factor"
	| factorNTS          => "factorNTS"
	| exprList           => "exprList"
	| exprListLparenNTS  => "exprListLparenNTS"
	| exprListNTS        => "exprListNTS"
	| monadicOpr         => "monadicOpr"
	| cpsStoDecl         => "cpsStoDecl"
	| cpsStoDeclNTS      => "cpsStoDeclNTS"
	| procDecl           => "procDecl"
	| procDeclNTS        => "procDeclNTS"

val string_of_gramsym = (string_of_term, string_of_nonterm)

local
  open FixFoxi.FixFoxiCore
in

val productions =
[
	(program,
		[[T PROGRAM, T IDENT, N globalNTS, T DO, N cpsCmd, T ENDPROGRAM]]),
	(globalNTS,
		[[T GLOBAL, N cpsDecl],
		 []]),
	(cpsDecl,
		[[N decl, N cpsDeclNTS]]),
	(cpsDeclNTS,
		[[T SEMICOLON, N decl, N cpsDeclNTS],
		 []]),
	(decl,
		[[N stoDecl],
		 [N funDecl],
		 [N procDecl]]),
	(stoDecl,
		[[N typedIdent],
		 [T CHANGEMODE, N typedIdent]]),
	(typedIdent,
		[[T IDENT, T COLON, T TYPE]]),
	(funDecl,
		[[T FUN, T IDENT, N paramList, T RETURNS, N stoDecl, N funDeclNTS, T DO, N cpsCmd, T ENDFUN]]),
	(funDeclNTS,
		 [[T LOCAL, N cpsStoDecl],
		  []]),
	(paramList,
		[[T LPAREN, N paramListNTS, T RPAREN]]),
	(paramListNTS,
		[[N param, N paramNTS],
		 []]),
	(paramNTS, 
		[[T COMMA, N param, N paramNTS],
		 []]),
	(param,
		[[N changeModeNTS, N mechModeNTS, N typedIdent]]),
	(changeModeNTS,
		[[T CHANGEMODE],
		 []]),
	(mechModeNTS,
		[[T MECHMODE],
		 []]),
	(cpsCmd,
		[[N cmd, N cpsCmdNTS]]),
	(cpsCmdNTS,
		[[T SEMICOLON, N cmd, N cpsCmdNTS],
		 []]),
	(cmd,
		[[T SKIP],
		 [N expr, T BECOMES, N expr],
		 [T IF, N expr, T THEN, N cpsCmd, N ifelseNTS, T ENDIF],
		 [T WHILE, N expr, T DO, N cpsCmd, T ENDWHILE],
		 [T SWITCH, N expr, N caseNTS, N defaultCaseNTS, T ENDSWITCH],
		 [T CALL, T IDENT, N exprList],
		 [T DEBUGIN, N expr],
		 [T DEBUGOUT, N expr]]),
	(ifelseNTS,
		[[T ELSE, N cpsCmd],
		 []]),
	(caseNTS,
		[[T CASE, T LITERAL, T COLON, N cpsCmd, T ENDCASE, N caseNTS],
		 []]),
	(defaultCaseNTS,
		[[T DEFAULTCASE, T COLON, N cpsCmd, T ENDCASE],
		 []]),
	(expr,
		[[N term0, N condExprNTS]]),
	(condExprNTS,
		[[T QUESTIONMARK, N expr, T COLON, N expr],
		 []]),
	(term0,
		[[N term1, N term0NTS]]),
	(term0NTS,
		[[T BOOLOPR, N term1, N term0NTS],
		 []]),
	(term1,
		[[N term2, N term1NTS]]),
	(term1NTS,
		[[T RELOPR, N term2],
		 []]),
	(term2,
		[[N term3, N term2NTS]]),
	(term2NTS,
		[[T ADDOPR, N term3, N term2NTS],
		 []]),
	(term3,
		[[N factor, N term3NTS]]),
	(term3NTS,
		[[T MULTOPR, N factor, N term3NTS],
		 []]),
	(factor,
		[[T LITERAL],
		 [T IDENT, N factorNTS],
	 	 [N monadicOpr, N factor],
 		 [T LPAREN, N expr, T RPAREN]]),
	(factorNTS,
		[[T INIT],
		 [N exprList],
		 []]),
	(exprList,
		[[T LPAREN, N exprListLparenNTS, T RPAREN]]),
	(exprListLparenNTS,
		[[N expr, N exprListNTS],
		 []]),
	(exprListNTS,
		[[T COMMA, N expr, N exprListNTS],
		 []]),
	(monadicOpr,
		[[T NOT],
		 [T ADDOPR]]),
	(cpsStoDecl,
		[[N stoDecl, N cpsStoDeclNTS]]),
	(cpsStoDeclNTS,
		[[T SEMICOLON, N stoDecl, N cpsStoDeclNTS],
		 []]),
	(procDecl,
		[[T PROC, T IDENT, N paramList, N procDeclNTS, T DO, N cpsCmd, T ENDPROC]]),
	(procDeclNTS,
		[[T LOCAL, N cpsStoDecl],
		 []])
]

val S = program

val result = fix_foxi productions S string_of_gramsym

end (* local *)














