package org.jasm.test.testclass;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TypeAnnotationClass<Q, @EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation T extends @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationInterface1 & TypeAnnotationInterface2> extends @EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation HashMap implements  Runnable, @EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation Appendable {
	
	private @EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation String text;
	
	public TypeAnnotationClass() {
		
	}
	
	public <F extends TypeAnnotationInterface1 & TypeAnnotationInterface2> TypeAnnotationClass(F arg) {
		arg.doSend1();
		arg.doSend1();
	}
	
	@Override
	public void run() {
		
		try {
			new Integer("123x");
		} catch (@EmptyInvisibleTypeAnnotation @EmptyVisibleTypeAnnotation IllegalArgumentException e) {
			System.err.println("Hallo");
		}
	}

	@Override
	public @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public void send(@EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationClass<Q,T> this, Object arg) {
		if (arg instanceof @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationInterface1) {
			
		}
		
	}
	
	public void send2(Integer a, @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation Double b) throws RuntimeException, @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation IllegalArgumentException {
		@EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation Object o = a;
		Integer i = (@EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation Integer)o;
		
		TypeAnnotationInterface3 arg = null;
		
		TypeAnnotationClass<String, TypeAnnotationInterface3> inst = new @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationClass<String, @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationInterface3>();
		TypeAnnotationClass.<String, @EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationInterface3>doSend(arg);
		
		TypeAnnotationClass<String, TypeAnnotationInterface3> inst2 = new  <@EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation TypeAnnotationInterface3>TypeAnnotationClass< String, TypeAnnotationInterface3>(arg);
		
		String path = "";
		try (@EmptyVisibleTypeAnnotation @EmptyInvisibleTypeAnnotation BufferedReader br =
                new BufferedReader(new FileReader(path))) {
				br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

interface TypeAnnotationInterface3 extends TypeAnnotationInterface1,TypeAnnotationInterface2 {
	
}
