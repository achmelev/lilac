package org.jasm.test.testclass;

public class TestBean {
	
	private short intValue;
	private boolean booleanValue;
	private String stringValue;
	private Runnable runnable;
	
	public TestBean(short intValue, boolean booleanValue, String stringValue, Runnable runnableValue) {
		this.intValue = intValue;
		this.booleanValue = booleanValue;
		this.stringValue = stringValue;
		this.runnable = runnableValue;
	}

	public short getIntValue() {
		return intValue;
	}

	public void setIntValue(short intValue) {
		this.intValue = intValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}
	
	

}
