package org.jasm.test.verify;

public class ConstrConcat implements IConstrConcat {
	
	private String content = null;
	
	public ConstrConcat() {
		
		
	}
	
	public void setContent() {
		int a = 0;
		String s = "world!";
		content = "Hello "+s+" : "+a;
	}

	/* (non-Javadoc)
	 * @see org.jasm.test.verify.IConstrConcat#getContent()
	 */
	@Override
	public String getContent() {
		return content;
	}
	
	

}
