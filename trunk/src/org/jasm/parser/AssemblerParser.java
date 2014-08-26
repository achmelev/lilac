package org.jasm.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jasm.item.clazz.Clazz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblerParser {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	List<ErrorMessage> errorMessages = new ArrayList<>();

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}
	
	
	public Clazz parse(InputStream inp) {
		
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
		ParseTree tree = parser.clazz();
		
		if (log.isDebugEnabled()) {
			log.debug("tree: "+tree.toStringTree());
			if (getErrorMessages().size() > 0) {
				StringBuffer buf = new StringBuffer();
				for (ErrorMessage erm: getErrorMessages()) {
					buf.append(erm.toString());
					buf.append("\n");
				}
				log.debug("\nsyntax error messages:\n"+buf.toString());
			}
		}
		
		return null;
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
