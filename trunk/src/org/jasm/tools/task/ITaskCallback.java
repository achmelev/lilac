package org.jasm.tools.task;

public interface ITaskCallback {
	
	public void printError(Task source, String message);
	public void printWarning(Task source, String message);
	public void printInfo(Task source, String message);
	public void failure(Task source);
	public void success(Task source);

}
