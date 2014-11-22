package org.jasm.test.testclass;

import java.io.IOException;
import java.util.HashMap;

public class TypeAnnotationClass<Q, @EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation T extends @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationInterface1 & TypeAnnotationInterface2> extends @EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation HashMap implements  Runnable, @EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation Appendable {
	
	private @EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation String text;

	@Override
	public void run() {
		
		
	}

	@Override
	public @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public void send(@EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationClass<Q,T> this) {
		
	}
	
	public void send2(Integer a, @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation Double b) throws RuntimeException, @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation IllegalArgumentException {
		
	}
	
	public static <R, @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation S extends TypeAnnotationInterface1 & @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationInterface2> void doSend(S arg) {
		arg.doSend1();
		arg.doSend2();
	}

	@Override
	public Appendable append(CharSequence csq) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appendable append(CharSequence csq, int start, int end)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appendable append(char c) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	

}

interface TypeAnnotationInterface1 {
	public void doSend1();
}

interface TypeAnnotationInterface2 {
	public void doSend2();
}
