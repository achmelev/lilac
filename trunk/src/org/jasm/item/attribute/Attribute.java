package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.Utf8Info;

public class Attribute extends AbstractByteCodeItem implements IContainerBytecodeItem<IAttributeContent>{
	
	private Utf8Info name = null;
	private IAttributeContent content = null;
	
	private int nameIndex = -1;
	
	private IByteBuffer sourceBuffer = null;
	private long contentOffset = -1;
	private int contentLength = -1;
	
	public Attribute() {
		
	}
	
	public Attribute(AbstractSimpleAttributeContent content, Utf8Info name) {
		this.name = name;
		this.content = content;
		this.content.setParent(this);
	}
	
	public Attribute(byte [] data, Utf8Info name) {
		this(new UnknownAttributeContent(data), name);
	}
	
	

	@Override
	public void read(IByteBuffer source, long offset) {
		this.nameIndex = source.readUnsignedShort(offset);
		long length = source.readUnsignedInt(offset+2);
		if (length > Integer.MAX_VALUE) {
			throw new RuntimeException("Attribute length more than "+length+" isn't supported!");
		}
		this.sourceBuffer = source;
		this.contentOffset = offset+6;
		this.contentLength = (int)length;
	}
	
	@Override
	protected void doResolve() {
		this.name = (Utf8Info)getConstantPool().get(this.nameIndex-1);
		this.content = selectContent();
		this.content.setParent(this);
		this.content.prepareRead(contentLength);
		this.content.read(sourceBuffer, contentOffset);
		this.content.setParent(this);
		this.content.resolve();

	}
	
	private IAttributeContent selectContent() {
		if (name.getValue().equals("ConstantValue")) {
			return new ConstantValueAttributeContent();
		} else if (name.getValue().equals("Exceptions")) {
		    return new ExceptionsAttributeContent();
		} else if (name.getValue().equals("InnerClasses")) {
		    return new InnerClassesAttributeContent();
		} else if (name.getValue().equals("EnclosingMethod")) {
		    return new EnclosingMethodAttributeContent();
		} else {
			return new UnknownAttributeContent();
		}
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, name.getIndexInPool());
		target.writeUnsignedInt(offset+2, content.getLength());
		content.write(target, offset+6);
	}

	@Override
	public int getLength() {
		if (content != null) {
			return 6+content.getLength();
		} else {
			return 6+contentLength;
		}
		
	}

	@Override
	public boolean isStructure() {
		return content.isStructure();
	}

	@Override
	public List<IPrintable> getStructureParts() {
		return content.getStructureParts();
	}

	@Override
	public String getPrintLabel() {
		return content.getPrintLabel();
	}

	@Override
	public String getPrintName() {
		return content.getPrintName();
	}

	@Override
	public String getPrintArgs() {
		return content.getPrintArgs();
	}

	@Override
	public String getPrintComment() {
		return content.getPrintComment();
	}



	@Override
	public int getSize() {
		return 1;
	}



	@Override
	public IAttributeContent get(int index) {
		if (index != 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return this.content;
	}



	@Override
	public int indexOf(IAttributeContent item) {
		if (item == content) {
			return 0;
		} else {
			return -1;
		}
	}

	public Utf8Info getName() {
		return name;
	}

	public IAttributeContent getContent() {
		return content;
	}
	
	
	

}
