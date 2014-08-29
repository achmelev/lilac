package org.jasm.item.constantpool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.Utf8String;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.parser.literals.StringLiteral;

public class Utf8Info extends AbstractConstantPoolEntry {
	
	private int length = -1;
	private StringLiteral valueLiteral;
	private String value = null;
	
	public Utf8Info() {
		
	}
	
	public Utf8Info(String value) {
		this.setValue(value);
	}

	@Override
	public short getTag() {
		return 1;
	}

	@Override
	public void doResolve() {

	}
	
	@Override
	protected void doResolveAfterParse() {
		throw new NotImplementedException("not implemented");
	}
	
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		updateLength();
	}
	
	private void updateLength() {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bo);
		
		try {
			dos.writeUTF(value);
			dos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		byte[] data = bo.toByteArray();
		this.length = data.length;
	}

	@Override
	public int getLength() {
		if (value == null) {
			throw new RuntimeException("No value set!");
		}
		return length;
	}

	@Override
	public void readBody(IByteBuffer source, long offset) {
		Utf8String value = source.readUTF8(offset);
		this.length = value.getLength()+1;
		this.value = value.getValue();
	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		target.writeUTF8(offset, value);
	}

	@Override
	public String toString() {
		return super.toString()+"("+value+")";
	}

	@Override
	public String getPrintName() {
		return "utf8info";
	}

	@Override
	public String getPrintArgs() {
		return PrettyPrinter.getJavaStyleString(getValue());
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	public void setValueLiteral(StringLiteral valueLiteral) {
		this.valueLiteral = valueLiteral;
	}
	
	
	

}
