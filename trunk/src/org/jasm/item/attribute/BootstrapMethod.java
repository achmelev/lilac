package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.MethodHandleInfo;
import org.jasm.item.constantpool.MethodHandleInfo.MethodHandleReferenceKind;
import org.jasm.item.constantpool.MethodTypeInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.literals.SymbolReference;

import com.sun.org.apache.bcel.internal.classfile.Method;

public class BootstrapMethod extends AbstractByteCodeItem implements ISymbolTableEntry {
	
	private int methodHandleIndex;
	private int[] paramIndexes; 
	
	private SymbolReference methodHandleReference;
	private MethodHandleInfo methodHandle;
	private List<SymbolReference> paramReferences; 
	private AbstractConstantPoolEntry[] params;
	
	private String symbolName = null;

	@Override
	public void read(IByteBuffer source, long offset) {
		methodHandleIndex = source.readUnsignedShort(offset);
		int size = source.readUnsignedShort(offset+2);
		paramIndexes = new int[size];
		long currentOffset = offset+4;
		for (int i=0;i<size; i++) {
			paramIndexes[i] = source.readUnsignedShort(currentOffset);
			currentOffset+=2;
		}
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, methodHandle.getIndexInPool());
		target.writeUnsignedShort(offset+2, params.length);
		long currentOffset = offset+4;
		for (int i=0;i<params.length; i++) {
			target.writeUnsignedShort(currentOffset, params[i].getIndexInPool());
			currentOffset+=2;
		}
		
	}

	@Override
	public int getLength() {
		int result = 4;
		if (params != null) {
			result+=params.length*2;
		} else if (paramIndexes != null) {
			result+=paramIndexes.length*2;
		}
		return result;
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
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "bootstrap method "+getDisassemblerLabel();
	}

	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		buf.append(methodHandle.getSymbolName());
		for (AbstractConstantPoolEntry param: params) {
			buf.append(", "+param.getSymbolName());
		}
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		methodHandle = (MethodHandleInfo)getConstantPool().get(methodHandleIndex-1);
		params = new AbstractConstantPoolEntry[paramIndexes.length];
		for (int i=0;i<paramIndexes.length; i++) {
			params[i] = getConstantPool().get(paramIndexes[i]-1);
		}
	}

	@Override
	protected void doResolveAfterParse() {
		methodHandle = getConstantPool().checkAndLoadFromSymbolTable(this, MethodHandleInfo.class, methodHandleReference);
		if (paramReferences != null) {
			params = new AbstractConstantPoolEntry[paramReferences.size()];
			for (int i=0;i<paramReferences.size(); i++) {
				params[i] = getConstantPool().checkAndLoadFromSymbolTable(this, new Class[]{StringInfo.class,ClassInfo.class, IntegerInfo.class,LongInfo.class,FloatInfo.class,DoubleInfo.class,MethodHandleInfo.class,MethodTypeInfo.class}, paramReferences.get(i));
			}
		}
	}

	public void setMethodHandleReference(SymbolReference methodHandleReference) {
		this.methodHandleReference = methodHandleReference;
	}
	
	public void addParamReference(SymbolReference ref) {
		if (paramReferences == null) {
			paramReferences = new ArrayList<SymbolReference>();
		}
		paramReferences.add(ref);
	}

	@Override
	public String getSymbolName() {
		if (symbolName != null) {
			return symbolName;
		} else {
			return getDisassemblerLabel();
		}
		
	}

	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}
	
	private String disassemblerLabel = null;
	
	private String getDisassemblerLabel() {
		if (disassemblerLabel == null) {
			String methodName = methodHandle.getReference().getDisassemblerLabel();
			if (methodName != null) {
				disassemblerLabel =  "bootstrap_"+((BootstrapMethodsAttributeContent)getParent()).getBootstrapNameGenerator().generateName(methodName);
			} else {
				disassemblerLabel =  "bootstrap_"+((BootstrapMethodsAttributeContent)getParent()).indexOf(this);
			}
		}
		return disassemblerLabel;
	}
}
