package org.jasm.item.constantpool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.Utf8String;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.IBytecodeItem;
import org.jasm.parser.literals.StringLiteral;
import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;


public class Utf8Info extends AbstractConstantPoolEntry {
	
	private int length = -1;
	private StringLiteral valueLiteral;
	private String value = null;
	
	public Utf8Info() {
		
	}

	@Override
	public short getTag() {
		return 1;
	}

	@Override
	public void doResolve() {

	}
	
	
	
	@Override
	protected void doVerify() {
		
		
	}

	@Override
	protected void doResolveAfterParse() {
		setValue(valueLiteral.getStringValue());
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
		this.length = data.length+1;
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
	public String getConstTypeLabel() {
		return "utf8";
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

	@Override
	protected String doGetDisassemblerLabel() {
		
		List<IBytecodeItem> allRefs = getConstantPool().getReferencingItems(this, IBytecodeItem.class); 
		
		Set<String> possibleNames = new HashSet<String>();
		for (IBytecodeItem it: allRefs) {
			if (it instanceof IUtf8ConstantPoolReference) {
				String candidate = ((IUtf8ConstantPoolReference)it).generateName(this);
				if (candidate != null) {
					possibleNames.add(candidate);
				}
			}
		}
		
		if (possibleNames.size() == 1) {
			return possibleNames.iterator().next();
		} else {
			try {
				MethodDescriptor desc = new MethodDescriptor(this.getValue());
				return "method_desc";
			} catch (IllegalDescriptorException e) {
				
			}
			try {
				TypeDescriptor desc = new TypeDescriptor(this.getValue());
				return "type_desc";
			} catch (IllegalDescriptorException e) {
				
			}
		}
		
		
		return null;
	}

	@Override
	public void completeGeneratedEntry() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
