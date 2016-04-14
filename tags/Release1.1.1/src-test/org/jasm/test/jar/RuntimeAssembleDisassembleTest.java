package org.jasm.test.jar;



import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RuntimeAssembleDisassembleTest extends
		AbstractAssembleDisassembleJarTestCase {


	
	
	@Test
	public void test() {
		doJarTest();
	}


	@Override
	protected boolean filter(String name) {
		return !name.contains("package-info") 
				//TODO - perhaps allow for different number of params in the descriptor and and param annotations attribute
				&& !name.equals("com/sun/xml/internal/ws/binding/WebServiceFeatureList$MergedFeatures.class")
				/*&& 
				name.equals("com/sun/jmx/snmp/IPAcl/ParserTokenManager.class")*/;
	}
	
	@Override
	protected File getFile() {
		return getFile(java.lang.Object.class);
	}
	
	
}
