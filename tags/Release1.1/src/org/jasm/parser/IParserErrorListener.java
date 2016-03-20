package org.jasm.parser;

public interface IParserErrorListener {
	
	public void clear();
	public void error(int line, int charPos, String msg);
	public void emitInternalError(int line, int charPos, String msg);
	public void flush();

}
