package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.SymbolReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Attribute extends AbstractByteCodeItem implements IContainerBytecodeItem<IAttributeContent>, IConstantPoolReference {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private SymbolReference nameReference = null;
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
		if (log.isDebugEnabled()) {
			log.debug("Read attribute "+this.hashCode()+"  offset="+offset+", contentOffset="+contentOffset);
		}
		this.contentLength = (int)length;
	}
	
	@Override
	protected void doResolve() {
		this.name = (Utf8Info)getConstantPool().get(this.nameIndex-1);
		this.content = selectContent();
		this.content.setParent(this);
		if (log.isDebugEnabled()) {
			log.debug("Reading (resolving) attribute's "+this.hashCode()+" content "+name.getValue()+" at "+contentOffset);
		}
		this.content.prepareRead(contentLength);
		this.content.read(sourceBuffer, contentOffset);
		this.content.setParent(this);
		this.content.resolve();

	}
	
	@Override
	protected void doResolveAfterParse() {
		if (this.nameReference != null) {
			name = getConstantPool().checkAndLoadFromSymbolTable(this, Utf8Info.class, nameReference);
		} else {
			this.name = selectNameEntry();
		}
		if (name != null) {
			this.content.resolve();
		}
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
		} else if (name.getValue().equals("Syntetic")) {
		    return new SynteticAttributeContent();
		} else if (name.getValue().equals("Signature")) {
		    return new SignatureAttributeContent();
		} else if (name.getValue().equals("SourceFile")) {
		    return new SourceFileAttributeContent();
		} else if (name.getValue().equals("Deprecated")) {
		    return new DeprecatedAttributeContent();
		} else if (name.getValue().equals("RuntimeInvisibleAnnotations")) {
		    return new RuntimeInvisibleAnnotationsAttributeContent();
		} else if (name.getValue().equals("RuntimeVisibleAnnotations")) {
		    return new RuntimeVisibleAnnotationsAttributeContent();
		} else if (name.getValue().equals("RuntimeInvisibleParameterAnnotations")) {
		    return new RuntimeInvisibleParameterAnnotationsAttributeContent();
		} else if (name.getValue().equals("RuntimeVisibleParameterAnnotations")) {
		    return new RuntimeVisibleParameterAnnotationsAttributeContent();
		} else if (name.getValue().equals("AnnotationDefault")) {
			return new AnnotationDefaultAttributeContent();
		} else if (name.getValue().equals("Code")) {
			return new CodeAttributeContent();
		} else if (name.getValue().equals("LineNumberTable")) {
			return new LineNumberTableAttributeContent();
		} else if (name.getValue().equals("LocalVariableTable")) {
			return new LocalVariableTableAttributeContent();
		} else if (name.getValue().equals("LocalVariableTypeTable")) {
			return new LocalVariableTypeTableAttributeContent();
		} else if (name.getValue().equals("StackMapTable")) {
			return new StackMapAttributeContent();
		} else {
			return new UnknownAttributeContent();
		}
		
	}
	
	private Utf8Info selectNameEntry() {
		String name = null;
		if (content instanceof SourceFileAttributeContent) {
			name = "SourceFile";
		} else if (content instanceof ConstantValueAttributeContent) {
			name = "ConstantValue";
		} else if (content instanceof ExceptionsAttributeContent) {
			name = "Exceptions";
		} else if (content instanceof SignatureAttributeContent) {
			name = "Signature";
		} else if (content instanceof SynteticAttributeContent) {
			name = "Synthetic";
		} else if (content instanceof DeprecatedAttributeContent) {
			name = "Deprecated";
		} else if (content instanceof RuntimeInvisibleAnnotationsAttributeContent) {
			name = "RuntimeInvisibleAnnotations";
		} else if (content instanceof RuntimeVisibleAnnotationsAttributeContent) {
			name = "RuntimeVisibleAnnotations";
		} else if (content instanceof RuntimeVisibleParameterAnnotationsAttributeContent) {
			name = "RuntimeVisibleParameterAnnotations";
		} else if (content instanceof RuntimeInvisibleParameterAnnotationsAttributeContent) {
			name = "RuntimeInvisibleParameterAnnotations";
		} else if (content instanceof InnerClassesAttributeContent) {
			name = "InnerClasses";
		} else if (content instanceof EnclosingMethodAttributeContent) {
			name = "EnclosingMethod";
		} else if (content instanceof CodeAttributeContent) {
			name = "Code";
		} else if (content instanceof AnnotationDefaultAttributeContent) {
			name = "AnnotationDefault";
		} else if (content instanceof StackMapAttributeContent) {
			name = "StackMapTable";
		} else if (content instanceof UnknownAttributeContent) {
			return getConstantPool().checkAndLoadFromSymbolTable(this, Utf8Info.class, nameReference);
		} else {
			throw new IllegalStateException("unknown attribute content type: "+content.getClass());
		}
		List<Utf8Info> infos = getConstantPool().getUtf8Infos(name);
		if (infos.size() == 0){
			emitError(null, "missing utf8info constant containing '"+name+"'");
			return null;
		} else {
			return infos.get(0);
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
	public String getTypeLabel() {
		return  content.getTypeLabel();
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
	
	public void setContent(IAttributeContent content) {
		content.setParent(this);
		this.content = content;
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{name};
	}
	
	

	

	@Override
	public String toString() {
		if (content == null) {
			return super.toString();
		} else {
			return "Attribute."+content.getClass().getSimpleName();
		}
		
	}

	@Override
	public SourceLocation getNextSourceLocation() {
		if ( getContent().getSourceLocation() != null) {
			return getContent().getSourceLocation();
		} else {
			return super.getNextSourceLocation();
		}
	}

	public void setNameReference(SymbolReference nameReference) {
		this.nameReference = nameReference;
	}
	
	
	

}
