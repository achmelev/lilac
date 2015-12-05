package org.jasm.test.parser;

import java.lang.annotation.Annotation;

import org.jasm.item.clazz.Clazz;
import org.junit.Test;

public class SimpleAnnotatedClassParseAndLoadTest extends
		AbstractParseAndLoadTestCase {

	@Override
	protected String getDateiName() {
		return "SimpleAnnotatedClassT.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.SimpleAnnotatedClassT";
	}

	@Override
	protected void testClass(Class cl) {
		Annotation[] anns = cl.getAnnotations();
		for (Annotation a: anns) {
			System.out.println(a.annotationType());
		}
		
		
	}
	
	@Test
	public void test() {
		doTest();
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		// TODO Auto-generated method stub
		
	}

}
