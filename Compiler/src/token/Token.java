package token;

public enum Token implements IToken { 
	LPAREN("LPAREN"),
	RPAREN("RPAREN"),
	COMMA("COMMA"),
	SEMICOLON("SEMICOLON"),
	COLON("COLON"),
	BECOMES("BECOMES"),
	CALL("CALL"),
	DEBUGIN("DEBUGIN"),
	DEBUGOUT("DEBUGOUT"),
	DO("DO"),
	ELSE("ELSE"),
	ENDFUN("ENDFUN"),
	ENDIF("ENDIF"),
	ENDPROC("ENDPROC"),
	ENDPROGRAM("ENDPROGRAM"),
	ENDWHILE("ENDWHILE"),
	FUN("FUN"),
	GLOBAL("GLOBAL"),
	IF("IF"),
	INIT("INIT"),
	LOCAL("LOCAL"),
	NOTOPR("NOTOPR"),
	PROC("PROC"),
	PROGRAM("PROGRAM"),
	RETURNS("RETURNS"),
	SKIP("SKIP"),
	THEN("THEN"),
	WHILE("WHILE"),
	SENTINEL("SENTINEL"),
	/*
	 * Case commands
	 */
	SWITCH("SWITCH"),
	ENDSWITCH("ENDSWITCH"),
	CASE("CASE"),
	DEFAULTCASE("DEFAULTCASE"),
	ENDCASE("ENDCASE"),
	/*
	 * Conditional expression
	 */
	QUESTIONMARK("QUESTIONMARK");
	
	private String value;

	Token(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}	
}
