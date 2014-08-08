package org.jasm.test.instructions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jasm.item.instructions.OpCodes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcodesTest {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream stream = cl.getResourceAsStream("org/jasm/test/instructions/opnames.txt");
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(stream));
		List<String> names = new ArrayList<>();
		try {
			String line = reader.readLine();
			int counter = 0;
			while (line != null) {
				short code = OpCodes.getOpcodeForName(line);
				String name = OpCodes.getNameForOpcode(code);
				Assert.assertEquals(line, name);
				counter++;
				if (!names.contains(line)) {
					names.add(line);
				} else {
					throw new RuntimeException(line+" is already there");
				}
				line = reader.readLine();
			}
			if (names.size() < OpCodes.getNames().size()) {
				log.debug("Unexpected names:");
				for (String s: OpCodes.getNames()) {
					if (!names.contains(s)) {
						log.debug(s);
					}
				}
				Assert.fail("Unexpected names, see above");
			} else if (names.size() > OpCodes.getNames().size()) {
				log.debug("Missing names:");
				for (String s: names) {
					if (!OpCodes.getNames().contains(s)) {
						log.debug(s);
					}
				}
				Assert.fail("Missing names, see above");
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
