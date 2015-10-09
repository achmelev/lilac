package org.jasm.test.testclass;

@TestAnnotation(stringValue="non\nDefault",booleanValue = false, byteValue = 1, charValue = 'c', enumValue=Days.MONDAY, clazzValue = Void.class, intValue = 5, longValue = 6, shortValue = 7, nestedAnnotation = @NestedAnnotation, intArrayValue = {2,5,6})
@TestInvisibleAnnotation
public interface SimpleAnnotatedInterface {
	
	
	
	@TestAnnotation(booleanValue = false, byteValue = 5, charValue = 'A', enumValue=Days.WEDNESDAY,clazzValue = Integer.class, intValue = 20, longValue = -60000L, shortValue = 700, nestedAnnotation = @NestedAnnotation, intArrayValue = {10,20,30})
	@TestInvisibleAnnotation
	public void annotatedMethod(int a);
	
	@TestAnnotation(booleanValue = false, byteValue = 5, charValue = 'A', enumValue=Days.WEDNESDAY,clazzValue = Integer.class, intValue = 20, longValue = -60000L, shortValue = 700, nestedAnnotation = @NestedAnnotation, intArrayValue = {10,20,30})
	@TestInvisibleAnnotation
	public void annotatedMethodWithParameters(int a,@TestInvisibleAnnotation  @TestAnnotation(booleanValue = false, byteValue = 5, charValue = 'A', enumValue=Days.WEDNESDAY,clazzValue = Integer.class, intValue = 20, longValue = -60000L, shortValue = 700, nestedAnnotation = @NestedAnnotation, intArrayValue = {10,20,30}) double b, float c);
	
	

}
