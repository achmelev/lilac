package org.jasm.test.jar;



import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class RuntimeAssembleDisassembleTest extends
		AbstractAssembleDisassembleJarForClassTestCase {


	
	
	@Test
	public void test() {
		doJarTest();
	}

	@Override
	protected Class getClazz() {
		return Object.class;
	}

	@Override
	protected boolean filter(String name) {
		return !name.contains("package-info") 
				//TODO - perhaps allow for different number of params in the descriptor and and param annotations attribute
				&& !name.equals("com/sun/xml/internal/ws/binding/WebServiceFeatureList$MergedFeatures.class")
				/*&& 
				name.equals("sun/rmi/transport/DGCImpl_Stub.class")*/;
	}
	
	
}
