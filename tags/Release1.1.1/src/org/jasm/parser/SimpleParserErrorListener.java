package org.jasm.parser;

import java.util.ArrayList;
import java.util.List;

public class SimpleParserErrorListener implements IParserErrorListener {
	
	private List<ErrorMessage> errors = new ArrayList<ErrorMessage>();

	@Override
	public void error(int line, int charPos, String msg) {
		errors.add(new ErrorMessage(line, charPos, msg));
		
	}
	
	@Override
	public void emitInternalError(int line, int charPos, String msg) {
		errors.add(new ErrorMessage(line, charPos, msg));
	}

	@Override
	public void flush() {
		if (errors.size() > 0) {
			StringBuffer buf = new StringBuffer();
			for (ErrorMessage erm: errors) {
				buf.append(erm.toString());
				buf.append("\n");
			}
			System.out.println(buf.toString());
			errors.clear();
		}
		
	}

	@Override
	public void clear() {
		errors.clear();
		
	}

	

}
