package org.jasm.test.jar;



import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Runtime7AssembleDisassembleTest extends
		AbstractAssembleDisassembleJarTestCase {


	
	
	@Test
	public void test() {
		doJarTest();
	}

	

	

	@Override
	protected File getFile() {
		return new File("c:/Programme/Java/jdk1.7.0_06/jre/lib/rt.jar");
	}
	
	@Override
	protected boolean filter(String name) {
		
		return !name.contains("package-info")
				//TODO - perhaps allow for different number of params in the descriptor and and param annotations attribute
				&& !name.equals("com/sun/xml/internal/ws/encoding/MtomCodec$ByteArrayBuffer.class")
				&& !name.equals("com/sun/xml/internal/ws/binding/WebServiceFeatureList$MergedFeatures.class")
				/*&& name.equals("com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$JFIFThumbPalette.class")*/;
				
	}
	
	
}
