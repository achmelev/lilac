package org.jasm.item.attribute;

import org.jasm.item.constantpool.Utf8Info;

public class SignatureAttributeContent extends AbstractStringAttributeContent {
	
	
	
	public SignatureAttributeContent() {
		super();
	}

	public SignatureAttributeContent(Utf8Info entry) {
		super(entry);
	}

	@Override
	public String getPrintName() {
		return "signature";
	}

	

}
