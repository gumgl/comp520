
//import Interpreter;
import golite.parser.* ;
import golite.lexer.* ;
import golite.node.* ;

import java.io.* ; 
import java.util.Map;
  
public class Main {
	static Map tokenMap;
	public static void main(String[] args) {
		if (args.length > 0) { 
			try { 
				/* Form our AST */ 
				Lexer lexer = new Lexer (new PushbackReader(new FileReader(args[0]), 1024)); 
				Parser parser = new Parser(lexer); 
				Start ast = parser.parse();
				PrintWriter stderr = new PrintWriter(System.out, true);
				
				TokenMapper tm = new TokenMapper();
			    ast.apply(tm);
			    tokenMap = tm.getMap();
			    
				// Get file name
				String path = args[0];
				if (path.endsWith(".min")) {
					path = path.substring(0, path.length() - 4);
				}
				// Pretty Print
				PrintWriter filePretty = new PrintWriter(new PrintWriter(path+".pretty.min"), true);
				PrettyPrinter pretty = new PrettyPrinter(filePretty) ; 
				ast.apply(pretty);
				filePretty.close();
				
				// Type Checker
				PrintWriter fileTypeChecker = new PrintWriter(new PrintWriter(path+".symbol.txt"), true);
				TypeChecker typechecker = new TypeChecker(fileTypeChecker, stderr) ; 
				ast.apply(typechecker);
				fileTypeChecker.close();
				
				if (typechecker.success) {
					// Generate C Code
					PrintWriter fileCCode = new PrintWriter(path+".c");
					CGenerator cgen = new CGenerator(fileCCode, typechecker.types, typechecker.symbols) ; 
					ast.apply(cgen);
					fileCCode.close();
					
					System.out.println("VALID, C code generated to " + path + ".c");
				}
				
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
			} catch (golite.parser.ParserException e) {
				System.out.println("INVALID (scanner error) at " + e.getMessage());
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
	public static Token getFirstToken(Node node) {
        if(node instanceof Token)
            return (Token) node;
        else
            return (Token) tokenMap.get(node);
    }
}
