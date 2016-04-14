package org.jasm.test.playground;

public class ExceptionProber implements Prober {

	@Override
	public int check(int a) {
		return 0;
	}

	/*@Override
	public int check(int a) {
		try {
			return doCheck(a);
		} catch (ProberException e) {
			
		}
	}
	
	private int doCheck(int a) throws ProberException {
		if (a == 2) {
			throw new ProberException();
		} else {
			return a;
		}
	}*/
	
	

}
