package org.jasm.item.attribute;

import org.jasm.item.constantpool.Utf8Info;

public class SourceFileAttributeContent extends AbstractStringAttributeContent {
	
	
	
	public SourceFileAttributeContent() {
		super();
	}

	public SourceFileAttributeContent(Utf8Info entry) {
		super(entry);
	}

	@Override
	public String getPrintName() {
		return "source file";
	}

}
