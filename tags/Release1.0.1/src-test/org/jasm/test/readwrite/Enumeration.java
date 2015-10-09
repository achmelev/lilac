package org.jasm.test.readwrite;

public enum Enumeration {
	
	SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY;
	
	private Enumeration value;
	
	Enumeration() {
		
	}
	
	Enumeration(Enumeration value) {
		this.value = value;
	}
	
	public Enumeration getValue() {
		return this.value;
	}
	
	
}
