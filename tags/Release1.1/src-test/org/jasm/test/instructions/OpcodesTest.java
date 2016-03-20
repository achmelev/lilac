package org.jasm.test.instructions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
				
				int categoryCounter = 0;
				if (OpCodes.isArgumentLessInstruction(code)) {
					categoryCounter++;
				}
				if (OpCodes.isBranchInstruction(code)) {
					categoryCounter++;
				}
				if (OpCodes.isConstantPoolInstruction(code)) {
					categoryCounter++;
				}
				if (OpCodes.isLocalVariableInstruction(code)) {
					categoryCounter++;
				}
				if (OpCodes.isShortLocalVariableInstruction(code)) {
					categoryCounter++;
				}
				if (OpCodes.isSpecialInstruction(code)) {
					categoryCounter++;
				}
				if (OpCodes.isWideBranchInstruction(code)) {
					categoryCounter++;
				}
				if (categoryCounter != 1) {
					Assert.fail(line+" in no category!");
				}
				
				counter++;
				if (!names.contains(line)) {
					names.add(line);
				} else {
					throw new RuntimeException(line+" is already there");
				}
				line = reader.readLine();
			}
			testUniquinessString(names);
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
			
			testUniquinessShort(OpCodes.getArgumentLessInstructions());
			testUniquinessShort(OpCodes.getBranchInstructions());
			testUniquinessShort(OpCodes.getConstantPoolInstructions());
			testUniquinessShort(OpCodes.getLocalVariableInstructions());
			testUniquinessShort(OpCodes.getSpecialInstructions());
			testUniquinessShort(OpCodes.getWideBranchInstructions());
			
			Assert.assertEquals(names.size(), OpCodes.getArgumentLessInstructions().size()+OpCodes.getBranchInstructions().size()+OpCodes.getConstantPoolInstructions().size()+OpCodes.getLocalVariableInstructions().size()+OpCodes.getShortLocalVariableInstructions().size()+OpCodes.getSpecialInstructions().size()+OpCodes.getWideBranchInstructions().size());
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void testUniquinessShort(List<Short> codes) {
		Set<Short> s = new HashSet<>();
		s.addAll(codes);
		for (Short s1: s) {
			if (codes.indexOf(s1) != codes.lastIndexOf(s1)) {
				Assert.fail(Integer.toHexString(s1)+" isn't unique");
			}
		}
		
	}
	
	private void testUniquinessString(List<String> codes) {
		Set<String> s = new HashSet<>();
		s.addAll(codes);
		for (String s1: s) {
			if (codes.indexOf(s1) != codes.lastIndexOf(s1)) {
				Assert.fail(s1+" isn't unique");
			}
		}
		
	}

}
