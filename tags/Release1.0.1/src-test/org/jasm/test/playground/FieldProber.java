package org.jasm.test.playground;

public class FieldProber implements Prober {

	@Override
	public int check(int a) {
		new FieldContainer().text = "XXX";
		return 0;
	}

}
