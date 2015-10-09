package org.jasm.test.readwrite;

import org.jasm.item.attribute.Attributes;
import org.jasm.item.attribute.IAttributeContent;
import org.jasm.item.attribute.SignatureAttributeContent;
import org.jasm.item.attribute.SourceFileAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericClassTest1 extends AbstractReadWriteTestCase {
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		doTest();
		
	}
	
	private <T extends IAttributeContent> T getAttributeContent(Attributes attrs, Class<T> clazz) {
		for (int i=0;i<attrs.getSize(); i++) {
			if (attrs.get(i).getContent().getClass().equals(clazz)) {
				return (T) attrs.get(i).getContent();
			}
		}
		return null;
	}

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/GenericClass.class";
	}

	@Override
	protected void doChecks(Clazz clazz) {
		Assert.assertEquals("<T:Ljava/lang/Object;>Ljava/lang/Object;", getAttributeContent(clazz.getAttributes(), SignatureAttributeContent.class).getValue());
		Assert.assertEquals("GenericClass.java", getAttributeContent(clazz.getAttributes(), SourceFileAttributeContent.class).getValue());
		Assert.assertEquals("(TT;)TT;", getAttributeContent(clazz.getMethods().get(1).getAttributes(), SignatureAttributeContent.class).getValue());
		
	}

}
