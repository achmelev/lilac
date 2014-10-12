package org.jasm.item.instructions;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class ArgumentLessInstruction extends AbstractInstruction  {
	
	public ArgumentLessInstruction() {
		
	}
	
	public ArgumentLessInstruction(short opCode) {
		super(opCode);
	}
	
	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		return null;
	}

	@Override
	public String getPrintArgs() {
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	public void read(IByteBuffer source, long offset) {

	}

	@Override
	public void write(IByteBuffer target, long offset) {

	}

	@Override
	protected void doResolve() {

	}
	
	@Override
	protected void doResolveAfterParse() {
		
	}

	

}
