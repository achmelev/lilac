package org.jasm.test.errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.parser.IParserErrorListener;

public class TestErrorsListener implements IParserErrorListener {
	
	private Map<Integer, List<String>> messages = new HashMap<Integer, List<String>>();

	@Override
	public void clear() {
		messages.clear();
	}

	@Override
	public void error(int line, int charPos, String msg) {
		List<String> lMessages = messages.get(line);
		if (lMessages == null) {
			lMessages = new ArrayList<String>();
			messages.put(line, lMessages);
		}
		lMessages.add(msg);
	}

	@Override
	public void flush() {
		
		
	}
	
	public List<String> getMessages(int line) {
		List<String> result = messages.get(line);
		if (result == null) {
			result = new ArrayList<String>();
		}
		return result;
	}

	@Override
	public void emitInternalError(int line, int charPos, String msg) {
		throw new RuntimeException(msg);
		
	}

}
