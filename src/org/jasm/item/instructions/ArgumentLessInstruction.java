package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class ArgumentLessInstruction extends AbstractInstruction implements ILocalVariableReference {
	
	public ArgumentLessInstruction() {
		
	}
	
	public ArgumentLessInstruction(short opCode) {
		super(opCode);
	}
	
	

	@Override
	public String getPrintName() {
		
		String name =  super.getPrintName();
		if (name.indexOf("load")==1 || name.indexOf("store") == 1) {
			name = "short "+name.substring(0, name.indexOf('_'));
		}
		return name;
		
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
		String name =  super.getPrintName();
		if (name.indexOf("load")==1 || name.indexOf("store") == 1) {
			return getLocalVariableReferences()[0].toString();
		} else {
			return null;
		}
		
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
	public LocalVariable[] getLocalVariableReferences() {
		String name = super.getPrintName();
		if (name.indexOf("load")==1 || name.indexOf("store") == 1) {
			
			String indexStr = name.substring(name.indexOf("_")+1, name.length());
			int index = Integer.parseInt(indexStr);
			char type = name.charAt(0);
			return new LocalVariable[]{new LocalVariable(index, type)};
			
		} else {
			return new LocalVariable[]{};
		}
	}

}
