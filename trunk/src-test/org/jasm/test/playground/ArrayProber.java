package org.jasm.test.playground;

public class ArrayProber implements Prober {

	@Override
	public int check(int a) {
		Boolean[] array = new Boolean[a];
		String s = array.toString();
		return 1;
	}

}
