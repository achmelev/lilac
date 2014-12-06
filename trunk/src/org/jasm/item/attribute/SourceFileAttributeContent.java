package org.jasm.item.attribute;

import org.jasm.item.constantpool.Utf8Info;

public class SourceFileAttributeContent extends AbstractStringAttributeContent {
	
	
	
	public SourceFileAttributeContent() {
		super();
	}

	@Override
	public String getPrintName() {
		return "source file";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

}
