package org.jasm.test.testclass;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
	
	String stringValue() default "dummy";
	byte byteValue();
	char charValue();
	short shortValue();
	int intValue();
	long longValue();
	boolean booleanValue();
	Days enumValue();
	Class clazzValue();
	int [] intArrayValue() ;
	NestedAnnotation nestedAnnotation();

}
