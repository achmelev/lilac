package org.jasm.test.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class FieldContainerParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "FieldContainer.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.playground.FieldContainer2";
	}

	@Override
	protected void testClass(Class cl) {
		 try {
			Object o = cl.newInstance();
			Map<String, Field> fields = new HashMap<String, Field>();
			for (Field f: cl.getDeclaredFields()) {
				fields.put(f.getName(), f);
			}
			
			Assert.assertEquals(Byte.TYPE, fields.get("byte_field").getType());
			Assert.assertEquals(Boolean.TYPE, fields.get("boolean_field").getType());
			Assert.assertEquals(Character.TYPE, fields.get("char_field").getType());
			Assert.assertEquals(Integer.TYPE, fields.get("int_field").getType());
			Assert.assertEquals(Long.TYPE, fields.get("long_field").getType());
			Assert.assertEquals(Float.TYPE, fields.get("float_field").getType());
			Assert.assertEquals(Double.TYPE, fields.get("double_field").getType());
			Assert.assertEquals(Short.TYPE, fields.get("short_field").getType());
			Assert.assertEquals(String.class, fields.get("string_field").getType());
			Assert.assertEquals(String.class, fields.get("string_field2").getType());
			
			Assert.assertEquals(Byte.TYPE, fields.get("byte_array_field").getType().getComponentType());
			Assert.assertEquals(Boolean.TYPE, fields.get("boolean_array_field").getType().getComponentType());
			Assert.assertEquals(Character.TYPE, fields.get("char_array_field").getType().getComponentType().getComponentType());
			Assert.assertEquals(Integer.TYPE, fields.get("int_array_field").getType().getComponentType());
			Assert.assertEquals(Long.TYPE, fields.get("long_array_field").getType().getComponentType());
			Assert.assertEquals(Float.TYPE, fields.get("float_array_field").getType().getComponentType());
			Assert.assertEquals(Double.TYPE, fields.get("double_array_field").getType().getComponentType());
			Assert.assertEquals(Short.TYPE, fields.get("short_array_field").getType().getComponentType());
			Assert.assertEquals(String.class, fields.get("string_array_field").getType().getComponentType());
			Assert.assertEquals(String.class, fields.get("string_array_field2").getType().getComponentType());
			
			Assert.assertTrue(Modifier.isPublic(fields.get("byte_field").getModifiers()));
			Assert.assertTrue(Modifier.isStatic(fields.get("byte_field").getModifiers()));
			Assert.assertTrue(Modifier.isProtected(fields.get("boolean_field").getModifiers()));
			Assert.assertTrue(Modifier.isPrivate(fields.get("char_field").getModifiers()));
			Assert.assertTrue(Modifier.isTransient(fields.get("char_field").getModifiers()));
			
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	protected boolean readAgain() {
		return false;
	}

	@Override
	protected boolean verify() {
		return true;
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		
		
	}
	
	

}
