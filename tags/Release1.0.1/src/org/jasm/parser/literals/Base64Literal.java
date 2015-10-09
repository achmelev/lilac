package org.jasm.parser.literals;

import org.apache.commons.codec.binary.Base64;


public class Base64Literal extends AbstractLiteral {

	public Base64Literal(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public byte[] getValue() {
		try {
			String encoded = getContent().substring(0,getContent().length()-1);
			return Base64.decodeBase64(encoded);
		} catch (RuntimeException e) {
			return null;
		}
	}
	

}
