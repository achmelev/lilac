package org.jasm.test.testclass;

public class ClassWithManyBranches {
	
	public void method1(int a, int b, int c) {
		if (a == b) {
			System.out.println("1"+c);
		} else if (a < b) {
			System.out.println("2"+c);
		} else {
			try {
				System.out.println("3"+c);
			} catch (RuntimeException e) {
				System.out.println("4"+c);
			} finally {
				System.out.println("5"+c);
			}
		}
	}

}
