package org.jasm.test.testclass;

@TestAnnotation(booleanValue = false, byteValue = 1, charValue = 'c', enumValue=Days.MONDAY, clazzValue = Void.class, intValue = 5, longValue = 6, shortValue = 7, nestedAnnotation = @NestedAnnotation, intArrayValue = {2,5,6})
@TestInvisibleAnnotation
public interface AnnotatedInterface {
	
	@TestAnnotation(stringValue="notDefault" ,booleanValue = true, byteValue = 2, charValue = 'a', enumValue=Days.TUESDAY, clazzValue = String.class, intValue = 100, longValue = 10000L, shortValue = 70, nestedAnnotation = @NestedAnnotation, intArrayValue = {40,-5,60})
	@TestInvisibleAnnotation
	public String annotatedField = null;
	
	public @TargetPathAnnotation String [] [] annotatedString = null;
	
	@TestAnnotation(booleanValue = false, byteValue = 5, charValue = 'A', enumValue=Days.WEDNESDAY,clazzValue = Integer.class, intValue = 20, longValue = -60000L, shortValue = 700, nestedAnnotation = @NestedAnnotation, intArrayValue = {10,20,30})
	@TestInvisibleAnnotation
	public void annotatedMethod(boolean b, @TestAnnotation(booleanValue = false, byteValue = 1, charValue = 2,enumValue=Days.THURSDAY, clazzValue = Void.class, intValue = 5, longValue = 6, shortValue = 7, nestedAnnotation = @NestedAnnotation, intArrayValue = {2,5,6})  @TestInvisibleAnnotation int a);
	

}
