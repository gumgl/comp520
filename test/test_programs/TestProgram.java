package test_programs;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import golite.Compiler;
import golite.GoLiteTypeException;
import golite.GoLiteWeedingException;
import golite.lexer.LexerException;
import golite.parser.ParserException;

@RunWith(Parameterized.class)
public class TestProgram {
	private String path;
	private boolean isValid;
	private CompilationStage executedStage;

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	public TestProgram(String path, boolean isValid, CompilationStage stage) {
		this.path = path;
		this.isValid = isValid;
		this.executedStage = stage;
		initializeException();
	}

	public void initializeException() {
		if (!isValid) {
			if (executedStage == null) {
				return;
			}

			switch (executedStage) {
			case LEXING:
				thrown.expect(LexerException.class);
				break;
			case PARSING:
				thrown.expect(ParserException.class);
				break;
			case WEEDING:
				thrown.expect(GoLiteWeedingException.class);
				break;
			case TYPE_CHECKING:
				thrown.expect(GoLiteTypeException.class);
				break;
			case CODE_GEN:
				// FIXME: Add real error
				break;
			default:
				throw new IllegalArgumentException("Unknown stage "+executedStage);
			}
		}
	}

	@Test
	public void testProgram() throws LexerException, IOException, ParserException {
		if (executedStage == null && !isValid) {
			System.err.println("Could not infer type of expected error for "+path);
			try {
				compile();
			} catch (LexerException | ParserException | GoLiteWeedingException | GoLiteTypeException e) {
				return;
			}
			fail("Expected a GoLite exception");
		}

		compile();
	}

	public void compile() throws ParserException, LexerException, IOException {
		Compiler compiler = new Compiler(path);

		switch (executedStage != null ? executedStage : CompilationStage.CODE_GEN) {
		// For now we run these as if they were the same
		// That's fine, because every passing scanner test
		// is also syntactically valid
		case LEXING:
		case PARSING:
			compiler.parseSource();
			break;

		case WEEDING:
			compiler.validateAST();
			break;

		// TODO: Add code generation as a separate stage
		case TYPE_CHECKING:
		default:
			compiler.typeCheck();
			break;
		}
	}

	/**
	 * Get a collection containing all the programs that should be tested
	 * @throws IOException
	 */
	@Parameters(name="{2}: {0} ({1})")
	public static Collection<Object[]> getPaths() throws IOException {
		String pathRoot = System.getProperty("golitetestpath");

		if (pathRoot == null)
			pathRoot = "programs/";

		return TestProgramIdentifier.findPrograms(pathRoot);
	}
}
