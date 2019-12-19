package scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import exception.LexicalError;
import token.Ident;
import token.IToken;
import token.Literal;
import token.Token;

public class Scanner {

	/*************************************************/

	private List<IToken> tokens = new ArrayList<>();
	private Map<String, String> map = new HashMap<>();

	/*************************************************/

	private final String PROPERTIES_FILE_PATH = "src/resources/token.properties";
	private final List<String> symbols = Arrays.asList("(", ")", ",", ";", ":", "=", "*", "+", "-", "/", "<", ">", "&", "|", "?");

	/*************************************************/

	public Scanner() {
		getTokenMapFromProperties();
	}
	
	/*************************************************/

	public List<IToken> scan(String path) throws LexicalError {
		/*
		 * Get CharSequence from iml file
		 */
		CharSequence cs = readImlFileAsString(path);
		
		/*
		 * Check basic condition for empty file or last item 'new line'
		 */
		assert cs.length() == 0 || cs.charAt(cs.length() - 1) == '\n';

		/*
		 * Used fields
		 */
		int state = 0;
		StringBuffer lexAccu = new StringBuffer(); // for constructing the identifier
		IToken lastToken = null;
		long numAccu= 0L; // for constructing the literal value
		
		int counterLines = 1;
		int counterPositionStart = 0;
		int counterPosition = 0;
		
		/*
		 * State machine for the characters
		 */
		for (int i = 0; i < cs.length(); i++) {
			char c= cs.charAt(i);
			
			// counter for exception
			if(c == '\n') {
				counterLines++;
				counterPositionStart = i;
			}
			counterPosition = i - counterPositionStart - 1;
			
			switch (state) {
				case 0:
					/*
					 *  State initial
					 */
					
					if (Character.isDigit(c)) {
						// change to state-digit
						state = 1;
						int digit= Character.digit(c, 10);
						numAccu= digit;
					} else if (isSymbol(c)) {
						// change to state symbol
						state = 2;
						i = i - 1;
					} else if (Character.isLetter(c)) {
						// change to state character
						state = 3;
						i = i - 1;
					} else if (c == ' ' || c == '\n' || c == '\r'){
						// ignore this character
					} else {
						throw new LexicalError("[" + counterLines + " : " + counterPosition + "] Unknown character! -> " + c + " ( " + Character.getName(c) + " )");
					}
					
					break;
				case 1:
					/*
					 * State digit
					 */			
					if (Character.isDigit(c) || '\'' == c) {
						state = 1;
						/*
						 * if not separator
						 */
						if('\'' != c) {
							int digit= Character.digit(c, 10);
							numAccu= numAccu * 10 + digit;
							
							// check if out of range
							if (numAccu > Integer.MAX_VALUE) {
								throw new LexicalError("[" + counterLines + " : " + counterPosition + "] Integer literal too large!");
							}
						}
					} else {
						state= 0;
						i= i - 1; // one back for next lexeme
						
						/*
						 * add the token to the list
						 */
						Literal tmp = new Literal();
						tmp.setIntValue((int)numAccu);
						tokens.add(tmp);
						System.out.println(tmp);
					}
					break;
				case 2:
					/*
					 * State Symbols
					 */
					System.out.println("Checking char: " + c);
					if (isSymbol(c)) {
						state = 2;
						
						if("//".equals(lexAccu.toString() + c)) {
							state = 4; 
							// reset state
							lastToken = null;
							lexAccu.delete(0,  lexAccu.length());
							break;
						}
						
						/*
						 * Check if a Token exists for the given symbol
						 */
						IToken symbolToken = getTokenFromString(lexAccu.toString() + c);
						
						// of token was not found
						if(symbolToken == null) {
							System.out.println("lastToken: " + lastToken);	
							
							if(lastToken != null) {
								i = i - 1;
								
								tokens.add(lastToken);
								System.out.println(lastToken);
								
								// reset state
								lastToken = null;
								lexAccu.delete(0,  lexAccu.length());
							} else {
								lexAccu.append(c);
							}
						} else {
							/*
							 * set token if token was found
							 */
							lastToken = symbolToken;							
							lexAccu.append(c);
						}
					} else {
						if(lastToken != null) {
							tokens.add(lastToken);
							state = 0;
							i = i - 1;
							// Setze die Werte zurück
							lastToken = null;
							lexAccu.delete(0,  lexAccu.length());							
						} else {
							throw new LexicalError("[" + counterLines + " : " + counterPosition + "] Symbol is not allowed: " + lexAccu);
						}
					}
					break;
				case 3:
					/*
					 * State Characters
					 */
					System.out.println("Checking if " + c + " is a IDENT or a KEYWORD");
					
					/*
					 * Check if still inside a word
					 */
					if(Character.isLetter(c) || Character.isDigit(c) || c == '\'' || c == '_' ) {
						state = 3;
						
						lexAccu.append(c);					
					} else {
						/*
						 * End of Word
						 */
						
						// get token if in list
						IToken token = getTokenFromString(lexAccu.toString());
						
						/*
						 * Set as identifier with value if not a keyword
						 */
						if(token == null) {
							/*
							 * Check if boolValue or Ident
							 */
							if("true".equals(lexAccu.toString()) || "false".equals(lexAccu.toString())) {
								Literal tmp = new Literal();
								tmp.setBoolValue(lexAccu.toString());
								token = tmp;
							} else {
								Ident tmp = new Ident();
								tmp.setValue(lexAccu.toString());
								token = tmp;
							}
							System.out.println(token);
						}
						
						// add the token to the list
						tokens.add(token);
						
						// reset the state
						lastToken = null;
						lexAccu.delete(0,  lexAccu.length());
						
						state = 0;
						i = i - 1; // one back for next lexeme						
						
					}
					break;
				case 4: 
					System.out.println("comment: " + c);
					/*
					 * State comment
					 */
					if(c == '\n')
						state = 0;
					break;
			}
		}
		
		/*
		 * Check final condition
		 */
		assert state == 0;
		
		/*
		 * Set SENTINEL token at the end
		 */
		tokens.add(Token.valueOf("SENTINEL"));
		
		/*
		 * Print list on the screen
		 */
		printValueOnConsole("scan", tokens.stream().map(Object::toString).collect(Collectors.joining(", ")));

		/*
		 * return ITOKEN-list
		 */
		return tokens;
	}
	
	/*************************************************/
	
	public boolean isSymbol(char c) {
		String s = String.valueOf(c);
		return symbols.contains(s);
	}
	
	/*************************************************/
	
	private IToken getTokenFromString(String string) {
		IToken token = null;
		
		if (map.containsKey(string)) {
			String[] validate = getSubstringsFromInput(map.get(string));

			token = getITokenFromPropertiesString(validate);
		}
		
		return token;
	}
	
	/*************************************************/

	private String readImlFileAsString(String path) {	
		String result = null;
		
		try {
			/*
			 * Read File-Content as String
			 */
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			// adding space to signal end of document
			result = new String(encoded, Charset.defaultCharset()) + " ";
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		printValueOnConsole("readImlFileAsString", result);
		
		return result;
	}
	
	/*************************************************/
	
	private void printValueOnConsole(String identifier, String outPut) {
		System.out.println("\n ********** " + identifier + "() ********** \n" + outPut + "\n **************************************************");
	}

	/*************************************************/

	private void getTokenMapFromProperties() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(PROPERTIES_FILE_PATH);

			// load a properties file
			prop.load(input);

			for (Object s : prop.keySet()) {
				map.put((String) s, prop.getProperty((String) s));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		printValueOnConsole("getTokenMapFromProperties", map.toString());
	}

	/*************************************************/

	private String[] getSubstringsFromInput(String s) {
		String[] strings = s.split("\\.");

		String tmp_enum_specific = strings[strings.length - 1];
		String tmp_enum_basic = s.substring(0, s.length() - tmp_enum_specific.length() - 1);

		printValueOnConsole("getSubstringsFromInput", Arrays.toString(strings));
		
		return new String[] { tmp_enum_basic, tmp_enum_specific };
	}

	/*************************************************/

	private IToken getITokenFromPropertiesString(String[] ss) {
		IToken token = null;
		
		try {
			Class clz = Class.forName(ss[0]);

			token = (IToken) Enum.valueOf(clz, ss[1].trim());

		} catch (IllegalArgumentException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		printValueOnConsole("getITokenFromPropertiesString", token.toString());
			
		return token;
	}

	/*************************************************/

}
