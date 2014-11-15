package org.jasm.test.jar;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAssembleDisassembleHttpJarTestCase extends
		AbstractHttpJarTestCase {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected void testClass(byte[] data) {
		
		String code = disassemble(data);
		byte[] data2 = assemble(code);
		//log.debug("code: \n"+code);
		assertArrayEquals(data, data2);
	}
	
	protected String disassemble(byte [] data) {
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		
		Clazz clazz = new Clazz();
		clazz.read(bbuf, 0L);
		clazz.resolve();
		clazz.updateMetadata();
		
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		PrettyPrinter printer = new PrettyPrinter(writer);
		printer.printItem(clazz);
		writer.close();
		
		return sw.toString();
	}
	
	protected byte [] assemble(String data) {
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream pr = new PrintStream(bo);
		pr.print(data);
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		
		AssemblerParser parser = null;
		parser = new AssemblerParser();
		Clazz clazz =  parser.parse(bi);
		if (parser.getErrorMessages().size() > 0) {
			log.debug("code: \n"+data);
			parser.debugErrors();
			Assert.fail("Parsing failed on:");
		}
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		return data2;
		
	}
	

}
