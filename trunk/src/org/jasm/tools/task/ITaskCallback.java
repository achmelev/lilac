package org.jasm.tools.task;

public interface ITaskCallback {
	
	public void printError(Runnable source, String message);
	public void printWarning(Runnable source, String message);
	public void printInfo(Runnable source, String message);
	public void failure(Runnable source);
	public void success(Runnable source);

}
