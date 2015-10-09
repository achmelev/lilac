package org.jasm.test.playground;

public class StackmapProber implements Prober {
	
	/* (non-Javadoc)
	 * @see org.jasm.test.playground.Prober#check(int)
	 */
	@Override
	public int check(int a) {
		if (a > 1) {
			return 1;
		} else {
			return -1;
		}
	}

}
