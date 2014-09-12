package org.jasm.test.testclass;

@TestAnnotation(booleanValue = false, byteValue = 1, charValue = 2, enumValue=Days.MONDAY,clazzValue = Void.class, intValue = 5, longValue = 6, shortValue = 7, nestedAnnotation = @NestedAnnotation, intArrayValue = {2,5,6})
@TestInvisibleAnnotation
public class AnnotatedClass {
	
	public void annotatedMethod(@TestAnnotation(booleanValue = false, byteValue = 1, charValue = 2, enumValue=Days.TUESDAY,clazzValue = Void.class, intValue = 5, longValue = 6, shortValue = 7, nestedAnnotation = @NestedAnnotation, intArrayValue = {2,5,6})  @TestInvisibleAnnotation int a) {
		
	}

}
