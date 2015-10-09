package org.jasm.test.jar;



import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Runtime6AssembleDisassembleTest extends
		AbstractAssembleDisassembleJarTestCase {

	@Test
	public void test() {
		doJarTest();
	}

	
	@Override
	protected File getFile() {
		return new File("c:/Programme/Java/jdk1.6.0_16/jre/lib/rt.jar");
	}
	
	@Override
	protected boolean filter(String name) {
		return !name.contains("package-info")
				&& !name.equals("com/sun/xml/internal/ws/binding/WebServiceFeatureList$MergedFeatures.class")
				&& !name.equals("com/sun/xml/internal/ws/encoding/MtomCodec$ByteArrayBuffer.class")
				/**&& name.equals("com/sun/org/apache/xerces/internal/impl/xpath/regex/Token.class")**/;
	}
	
	
}
