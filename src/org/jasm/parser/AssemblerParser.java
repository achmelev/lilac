package org.jasm.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.JavaAssemblerParser.ClazzContext;
import org.jasm.parser.JavaAssemblerParser.VersionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblerParser  extends JavaAssemblerBaseListener {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<ErrorMessage> errorMessages = new ArrayList<>();
	
	private Clazz result = null;
	
	Stack<IBytecodeItem> stack = new Stack<>();

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}
	
	
	public Clazz parse(InputStream inp) {
		
		result = null;
		errorMessages.clear();
		stack.clear();
		
		ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(inp);
		} catch (IOException e) {
			throw new RuntimeException("Error creating antlr stream",e);
		}
				
		JavaAssemblerLexer lexer = new JavaAssemblerLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaAssemblerParser parser = new JavaAssemblerParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new SyntaxErrorListener(this));
		
		//Parse
		ParseTree tree = parser.clazz();
		
		if (log.isDebugEnabled()) {
			log.debug("tree: "+tree.toStringTree());
		}
		debugErrors();
		
		
		
		if (errorMessages.size() == 0) {
			//Walk tree an create class
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(this, tree);
			debugErrors();
			if (errorMessages.size() == 0) {
				//resolve and validate symbolic references
				//TODO
				
			} else {
				result = null;
			}
		}
		
		
		
		return result;
	}
	
	private void debugErrors() {
		if (log.isDebugEnabled()) {
			if (getErrorMessages().size() > 0) {
				StringBuffer buf = new StringBuffer();
				for (ErrorMessage erm: getErrorMessages()) {
					buf.append(erm.toString());
					buf.append("\n");
				}
				log.debug("\nsyntax error messages:\n"+buf.toString());
			}
		}
	}


	@Override
	public void enterClazz(ClazzContext ctx) {
		stack.push(new Clazz());
	}


	@Override
	public void exitClazz(ClazzContext ctx) {
		result = (Clazz)stack.pop();
	}


	@Override
	public void enterVersion(VersionContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		String version = ctx.VersionLiteral().getText();
		int lineNumber = ctx.VersionLiteral().getSymbol().getLine();
		int charPosition = ctx.VersionLiteral().getSymbol().getCharPositionInLine();
		try {
			clazz.setVersion(version);
		} catch (IllegalArgumentException e) {
			errorMessages.add(new ErrorMessage(lineNumber, charPosition, e.getMessage()));
		}
	}
	
	
	
	
	

}

class SyntaxErrorListener extends BaseErrorListener {
	
	private AssemblerParser parent;
	
	SyntaxErrorListener(AssemblerParser parent) {
		this.parent = parent;
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer,
			Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		parent.getErrorMessages().add(new ErrorMessage(line, charPositionInLine, msg));
	}
	
}
