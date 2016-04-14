package org.jasm.test.testclass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.CONSTRUCTOR, ElementType.FIELD,ElementType.LOCAL_VARIABLE,ElementType.METHOD,ElementType.PARAMETER,ElementType.TYPE_PARAMETER,ElementType.TYPE_USE,ElementType.TYPE,ElementType.ANNOTATION_TYPE})
public @interface EmptyInvisibleTypeAnnotation {

}
