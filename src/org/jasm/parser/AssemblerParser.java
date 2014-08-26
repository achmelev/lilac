package org.jasm.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jasm.item.clazz.Clazz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblerParser {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	List<String> errorMessages = new ArrayList<>();

	public List<String> getErrorMessages() {
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
		ParseTree tree = parser.clazz();
		
		if (log.isDebugEnabled()) {
			log.debug("tree: "+tree.toStringTree());
		}
		
		return null;
	}
	
	

}
