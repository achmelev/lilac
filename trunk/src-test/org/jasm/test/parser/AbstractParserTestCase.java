package org.jasm.test.parser;

import java.io.InputStream;

import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;

public abstract class AbstractParserTestCase {
	
	protected Clazz parse() {
		InputStream inp = this.getClass().getClassLoader().getResourceAsStream("org/jasm/test/parser/"+getDateiName());
		return new AssemblerParser().parse(inp);
	}
	
	protected abstract String getDateiName();

}
