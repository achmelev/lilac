package org.jasm.item.instructions;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class NewarrayInstruction extends AbstractInstruction {
	
	private short type = -1;
	
	public NewarrayInstruction() {
		super(OpCodes.newarray);
	}
	
	public NewarrayInstruction(String typeName) {
		this(getTypeCode(typeName));
	}
	
	public NewarrayInstruction(short type) {
		super(OpCodes.newarray);
		if (type<JasmConsts.ARRAY_TYPE_BOOLEAN || type>JasmConsts.ARRAY_TYPE_LONG) {
			throw new IllegalArgumentException(type+"");
		}
		this.type = type;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		type = source.readUnsignedByte(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, type);

	}

	@Override
	public int getLength() {
		return 2;
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
		return getTypeName(type);
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {


	}
	
	@Override
	protected void doResolveAfterParse() {
		
	}
	
	private String getTypeName(short type) {
		if (type == JasmConsts.ARRAY_TYPE_BOOLEAN) {
			return JasmConsts.TYPENAME_BOOLEAN;
		} else if (type == JasmConsts.ARRAY_TYPE_BYTE) {
			return JasmConsts.TYPENAME_BYTE;
		} else if (type == JasmConsts.ARRAY_TYPE_CHAR) {
			return JasmConsts.TYPENAME_CHAR;
		} else if (type == JasmConsts.ARRAY_TYPE_DOUBLE) {
			return JasmConsts.TYPENAME_DOUBLE;
		} else if (type == JasmConsts.ARRAY_TYPE_FLOAT) {
			return JasmConsts.TYPENAME_FLOAT; 
		} else if (type == JasmConsts.ARRAY_TYPE_INT) {
			return JasmConsts.TYPENAME_INT; 
		} else if (type == JasmConsts.ARRAY_TYPE_LONG) {
			return JasmConsts.TYPENAME_LONG; 
		} else if (type == JasmConsts.ARRAY_TYPE_SHORT) {
			return JasmConsts.TYPENAME_SHORT; 
		} else {
			throw new IllegalArgumentException(type+"");
		}
	}
	
	private static short getTypeCode(String typeName) {
		if (typeName .equals(JasmConsts.TYPENAME_BOOLEAN)) {
			return JasmConsts.ARRAY_TYPE_BOOLEAN;
		} else if (typeName .equals(JasmConsts.TYPENAME_BYTE)) {
			return JasmConsts.ARRAY_TYPE_BYTE;
		} else if (typeName .equals(JasmConsts.TYPENAME_CHAR)) {
			return JasmConsts.ARRAY_TYPE_CHAR;
		} else if (typeName .equals(JasmConsts.TYPENAME_DOUBLE)) {
			return JasmConsts.ARRAY_TYPE_DOUBLE;
		} else if (typeName .equals(JasmConsts.TYPENAME_FLOAT)) {
			return JasmConsts.ARRAY_TYPE_FLOAT;
		} else if (typeName .equals(JasmConsts.TYPENAME_INT)) {
			return JasmConsts.ARRAY_TYPE_INT; 
		} else if (typeName .equals(JasmConsts.TYPENAME_LONG)) {
			return JasmConsts.ARRAY_TYPE_LONG;
		} else if (typeName .equals(JasmConsts.TYPENAME_SHORT)) {
			return JasmConsts.ARRAY_TYPE_SHORT;
		} else {
			throw new IllegalArgumentException(typeName+"");
		}
	}

	public short getType() {
		return type;
	}
	
	

}
