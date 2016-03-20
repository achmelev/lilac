package org.jasm.item.instructions.verify.types;

public interface IClassQuery {
	
	public boolean isInterface(String className);
	public boolean isAssignable(String classTo, String classFrom);
	public String merge(String classTo, String classFrom);

}
