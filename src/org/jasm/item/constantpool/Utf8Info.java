package org.jasm.item.constantpool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.IllegalSelectorException;
import java.util.List;

import javassist.bytecode.MethodInfo;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.Utf8String;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.attribute.InnerClass;
import org.jasm.item.clazz.AbstractClassMember;
import org.jasm.item.clazz.Field;
import org.jasm.item.clazz.Method;
import org.jasm.parser.literals.StringLiteral;


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
		
		List<ClassInfo> refs = getConstantPool().getReferencingItems(this, ClassInfo.class); 
		if (refs.size()>0) {
			ClassInfo ref = refs.get(0);
			if (ref.getDisassemblerLabel() !=null) {
				return ref.getDisassemblerLabel()+"_name";
			}
		}
		//name or desc
		List<AbstractRefInfo> refs1 = getConstantPool().getReferencingItems(this, AbstractRefInfo.class); 
		if (refs1.size()>0) {
			
			AbstractRefInfo ref = refs1.get(0);
			
			String result = "";
			if (ref.getNameAndTypeReference().getNameReference() == this ) {
				result = ref.getNameAndTypeReference().getNameReference().getValue();
				if (result.equals("<init>")) {
					result = "_init";
				} else if (result.equals("<clinit>")) {
					result = "_clinit";
				}
				result = result+"_name";
			} else if (ref.getNameAndTypeReference().getDescriptorReference() == this && allRefs.size() ==2) {
				result = ref.getNameAndTypeReference().getNameReference().getValue()+"_desc";
			} else if (ref.getNameAndTypeReference().getDescriptorReference() == this) {
				if (ref instanceof MethodrefInfo) {
					result =  "method_desc";
				} else if (ref instanceof FieldrefInfo){
					result = "type_desc";
				} 
			} else {
				throw new IllegalStateException();
			}
			return result;
			
			
		}
		
		List<InnerClass> refs3 = getConstantPool().getReferencingItems(this, InnerClass.class); 
		if (refs3.size() > 0) {
			InnerClass ref = refs3.get(0);
			if (ref.getInnerName() == this) {
				return ref.getInnerName().getValue()+"_name";
			}
		}
		
		List<AbstractClassMember> refs4 = getConstantPool().getReferencingItems(this, AbstractClassMember.class); 
		if (refs4.size() > 0) {
			AbstractClassMember ref = refs4.get(0);
			String result =  ref.getName().getValue();
			if (result.equals("<init>")) {
				result = "_init";
			} else if (result.equals("<clinit>")) {
				result = "_clinit";
			}
			if (ref.getName() == this) {
				return result+"_name";
			} else if (ref.getDescriptor() == this) {
				if (ref instanceof Method) {
					result =  "method_desc";
				} else if (ref instanceof Field){
					result = "type_desc";
				}
			}
		}
		
		return null;
	}
	
	
	

}
