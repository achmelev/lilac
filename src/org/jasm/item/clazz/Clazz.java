package org.jasm.item.clazz;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.ATR;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.attribute.Attributes;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;


public class Clazz extends AbstractByteCodeItem implements IContainerBytecodeItem<IBytecodeItem> {
	
	
	private int majorVersion = -1;
	private int minorVersion = -1;
	private ConstantPool pool = null;
	private int accessFlags = 0;
	private ClassInfo thisClass; 
	private int thisClassIndex = -1;
	private ClassInfo superClass;
	private int superClassIndex = -1;
	private List<ClassInfo> interfaces;
	private List<Integer> interfacesIndexes = null;
	private Attributes attributes = null;
	
	private List<IBytecodeItem> children = new ArrayList<>();
	
	
	public Clazz() {
		initChildren();
	}
	
	public Clazz(int minorVersion, int majorVersion) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		initChildren();
	}
	
	private void initChildren() {
		pool = new ConstantPool();
		pool.setParent(this);
		children.add(pool);
		attributes = new Attributes();
		attributes.setParent(this);
		children.add(attributes);
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		long currentOffset = offset;
		long magic = source.readUnsignedInt(currentOffset);
		if (magic != 0xCAFEBABEL) {
			throw new IllegalArgumentException("Unexpected starting bytes: "+Long.toHexString(magic));
		}
		currentOffset+=4;
		this.minorVersion = source.readUnsignedShort(currentOffset);
		this.majorVersion = source.readUnsignedShort(currentOffset+2);
		currentOffset+=4;
		pool.read(source, currentOffset);
		currentOffset+=pool.getLength();
		accessFlags = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		thisClassIndex = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		superClassIndex = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		int interfacesLength = source.readUnsignedShort(currentOffset);
		interfacesIndexes = new ArrayList<>();
		for (int i=0;i<interfacesLength; i++) {
			currentOffset+=2;
			interfacesIndexes.add(source.readUnsignedShort(currentOffset));
		}
		currentOffset+=2;
		int fieldsCount = source.readUnsignedShort(currentOffset);
		if (fieldsCount !=0) {
			throw new IllegalArgumentException("fieldsCount="+fieldsCount);
		}
		currentOffset+=2;
		int methodsCount = source.readUnsignedShort(currentOffset);
		if (methodsCount !=0) {
			throw new IllegalArgumentException("methodsCount="+fieldsCount);
		}
		currentOffset+=2;
		attributes.read(source, currentOffset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		long currentOffset = offset;
		target.writeUnsignedInt(currentOffset, 0xCAFEBABEL);
		target.writeUnsignedShort(currentOffset+4, minorVersion);
		target.writeUnsignedShort(currentOffset+6, majorVersion);
		currentOffset+=8;
		pool.write(target, currentOffset);
		currentOffset+=pool.getLength();
		target.writeUnsignedShort(currentOffset, accessFlags);
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, pool.indexOf(thisClass)+1);
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, pool.indexOf(superClass)+1);
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, interfaces.size());
		for (ClassInfo cl: interfaces) {
			currentOffset+=2;
			target.writeUnsignedShort(currentOffset, pool.indexOf(cl)+1);
		}
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, 0);
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, 0);
		currentOffset+=2;
		attributes.write(target, currentOffset);
	}

	@Override
	public int getLength() {
		int result = 8;
		result+=pool.getLength();
		result+=6;
		result+=2;
		result+=interfaces.size()*2;
		result+=4;
		result+=attributes.getLength();
		return result;
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<IPrintable>();
		
		result.add(new SimplePrintable(null, "version", new String[]{majorVersion+"."+minorVersion}, (String)null));
		result.add(new SimplePrintable(null, "name", new String[]{"#"+thisClass.getPrintLabel()}, thisClass.getClassName()));
		result.add(new SimplePrintable(null, "extends", new String[]{"#"+superClass.getPrintLabel()}, superClass.getClassName()));
		if (interfaces != null && interfaces.size()>0) {
			String [] comment = new String[interfaces.size()];
			String[] args = new String[interfaces.size()];
			for (int i=0;i<interfaces.size(); i++) {
				args[i] = "#"+interfaces.get(i).getIndexInPool();
				comment[i] = interfaces.get(i).getClassName();
		
			}
			result.add(new SimplePrintable(null, "implements", args, comment));
			
		}
		result.add(new SimplePrintable(null, "access", new String[]{"TODO"}, (String)null));
		result.add(pool);
		result.add(attributes);
		
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "class";
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
	protected void doResolve() {
		pool.resolve();
		this.thisClass = (ClassInfo)pool.get(this.thisClassIndex-1);
		this.superClass = (ClassInfo)pool.get(this.superClassIndex-1);
		this.interfaces = new ArrayList<>();
		for (int i=0;i<interfaces.size(); i++) {
			interfaces.add((ClassInfo)pool.get(interfacesIndexes.get(i)-1));
		}
		attributes.resolve();
	}

	@Override
	public ConstantPool getConstantPool() {
		return pool;
	}

	@Override
	public boolean isRoot() {
		return true;
	}
	
	//container
	@Override
	public int getSize() {
		return children.size();
	}

	@Override
	public IBytecodeItem get(int index) {
		return children.get(index);
	}

	@Override
	public int indexOf(IBytecodeItem item) {
		return children.indexOf(item);
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public ClassInfo getThisClass() {
		return thisClass;
	}
	
	public void setThisClass(ClassInfo thisClass) {
		this.thisClass = thisClass;
	}

	public ClassInfo getSuperClass() {
		return superClass;
	}
	
	public void setSuperClass(ClassInfo superClass) {
		this.superClass = superClass;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public List<ClassInfo> getInterfaces() {
		return interfaces;
	}

}
