
//import Interpreter; 
import golite.parser.* ; 
import golite.lexer.* ; 
import golite.node.* ; 

import java.io.* ; 
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
  
public class Main {
	static Map tokenMap;
	static String path;
	static PrintWriter stderr;
	public static void main(String[] args) {
		if (args.length > 0) { 
			try { 
				/* Build AST */ 
				Lexer lexer = new ConservingGoLexer(new PushbackReader(new FileReader(args[0]), 1024));
				/*Parser parser = new Parser(lexer); 
				Start ast = parser.parse();
				
				stderr = new PrintWriter(System.out, true);
				
				TokenMapper tm = new TokenMapper();
			    ast.apply(tm);
			    tokenMap = tm.getMap();*/
			    
				// Get file name
				path = args[0];
				if (path.endsWith(".min")) {
					path = path.substring(0, path.length() - 4);
				}
				
				TokenPrintHtml(lexer);
				
				//PrettyPrint(ast);
				
				/*if (TypeCheck(ast)) {
					// Generate C Code
					PrintWriter fileCCode = new PrintWriter(path+".c");
					CodeGenerator cgen = new CodeGenerator(fileCCode, typechecker.types, typechecker.symbols) ; 
					ast.apply(cgen);
					fileCCode.close();
					
					System.out.println("VALID, C code generated to " + path + ".c");
				} */
				
				/*
				// Display Typed AST in console
				ASTPrinter typed = new ASTPrinter(typechecker.types);
				ast.apply(typed);
				
				// Display AST in a JTree
				ASTDisplay display = new ASTDisplay();
				ast.apply(display);
				
				// Print Symbol Table
				for (String id : typechecker.symbols.keySet())
					System.out.println("<" + id + "> is " + String.valueOf(typechecker.symbols.get(id)));
				*/
			} catch (golite.lexer.LexerException e) {
				System.out.println("INVALID (lexer error) at " + e.getMessage());
			/*} catch (golite.parser.ParserException e) { 
				System.out.println("INVALID (scanner error) at " + e.getMessage());*/
			} catch (FileNotFoundException e) {
				System.out.println("Error: File \"" + args[0] + "\" not found.");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		} else { 
			System.err.println("Usage: golite inputFile"); 
			System.exit(1);
		}
	}
	/*
	 * Generates a .html file to see tokens
	 */
	public static void TokenPrintHtml(Lexer lexer) throws LexerException, IOException {
		PrintWriter fileHtml = new PrintWriter(new PrintWriter(path+".tokens.html"), true);

		try {
			StringBuilder html = new StringBuilder("<html><body><pre>");
			int count=0; // To prevent some infinite loops
			while (lexer.peek() != null && count < 2000) {
				count ++;
				Token token = lexer.next();
				String color = count % 2 == 0 ? "LightSkyBlue" : "LightSalmon";

				html.append("<span style=\"background-color:"+color+"\" title=\"" + token.getClass().getSimpleName() + "\">" + token.getText().replaceAll(" ", "&#32;").replaceAll("(\r\n|\n)", "&#x23CE;$1") + "</span>");
			}
			html.append("</pre></body></html>");
			fileHtml.write(html.toString());
		} finally {
			fileHtml.close();
		}
	}

	public static void TokenPrintText(Lexer lexer) throws LexerException, IOException {
		int count=0; // To prevent some infinite loops
        while (lexer.peek() != null && count < 2000) {
        	count ++;
        	Token token = lexer.next();
        	System.out.println(token.getLine() + ": " + token.getClass().getSimpleName() + "(" + token.getText() + ")" );
        }
	}
	
	public static void PrettyPrint(Node ast) throws FileNotFoundException {
		// Pretty Print
		PrintWriter filePretty = new PrintWriter(new PrintWriter(path+".pretty.golite"), true);
		PrettyPrinter pretty = new PrettyPrinter(filePretty) ; 
		ast.apply(pretty);
		filePretty.close();
	}
	
	public boolean TypeCheck(Node ast) throws FileNotFoundException {
		PrintWriter fileTypeChecker = new PrintWriter(new PrintWriter(path+".symbol.txt"), true);
		TypeChecker typechecker = new TypeChecker(fileTypeChecker, stderr) ; 
		ast.apply(typechecker);
		fileTypeChecker.close();
		return typechecker.success;
	}
	
	public static Token getFirstToken(Node node) {
        if(node instanceof Token)
            return (Token) node;
        else
            return (Token) tokenMap.get(node);
    }
}
