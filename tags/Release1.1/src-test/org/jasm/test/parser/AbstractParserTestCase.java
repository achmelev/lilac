package org.jasm.test.parser;

import java.io.InputStream;

import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.SimpleParserErrorListener;
import org.jasm.test.macro.TestMacroFactory;
import org.junit.Assert;

public abstract class AbstractParserTestCase {
	
	protected AssemblerParser parser = null;
	
	protected Clazz parse() {
		InputStream inp = this.getClass().getClassLoader().getResourceAsStream("org/jasm/test/parser/"+getDateiName());
		parser = new AssemblerParser();
		parser.setMacroFactory(new TestMacroFactory());
		parser.addErrorListener(new SimpleParserErrorListener());
		return parser.parse(inp);
	}
	
	protected abstract String getDateiName();
	
	protected Clazz doTest() {
		Clazz clazz = parse();
		if (parser.getErrorCounter() > 0) {
			parser.flushErrors();
			Assert.fail("Parsing failed!");
		} 
		Assert.assertNotNull(clazz);
		return clazz;
	}
	
	

}
