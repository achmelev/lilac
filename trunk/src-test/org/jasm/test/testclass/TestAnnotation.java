package org.jasm.test.testclass;

public @interface TestAnnotation {
	
	String stringValue() default "dummy";
	byte byteValue();
	char charValue();
	short shortValue();
	int intValue();
	long longValue();
	boolean booleanValue();
	Class clazzValue();
	//int [] intArrayValue() ;
	NestedAnnotation nestedAnnotation();

}
