package org.jasm.item.constantpool;



import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;


public abstract class AbstractReferenceEntry extends AbstractConstantPoolEntry {

	private int[] index = null;
	private AbstractConstantPoolEntry[] reference = null;
	
	protected AbstractReferenceEntry() {
		
	}
	
	protected AbstractReferenceEntry(AbstractConstantPoolEntry[] reference) {
		this.setReference(reference);
	}

	@Override
	public int getLength() {
		return 1 + getNumberOfReferences()*2;
	}

	@Override
	public void readBody(IByteBuffer source, long offset) {
		this.index = new int[this.getNumberOfReferences()];
		for (int i=0;i<index.length; i++) {
			index [i] = source.readUnsignedShort(offset+i*2);
		}
	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		for (int i=0;i<index.length; i++) {
			target.writeUnsignedShort(offset+i*2, index[i]);
		}
	}

	

	@Override
	protected void doResolve() {
		reference = new AbstractConstantPoolEntry[getNumberOfReferences()];
		for (int i=0; i<index.length; i++) {
			reference[i] = (AbstractConstantPoolEntry)getParent().get(index[i]-1);
		}
	}

	
	
	protected AbstractConstantPoolEntry[] getReference() {
		return reference;
	}

	

	protected void setReference(AbstractConstantPoolEntry[] reference) {
		if (reference.length!=getNumberOfReferences()) {
			throw new IllegalArgumentException("illegal number of references "+reference.length+" != "+getNumberOfReferences());
		}
		this.reference = reference;
		updateIndex();
	}
	
	private void updateIndex() {
		this.index = new int[getNumberOfReferences()];
		for (int i=0;i<index.length; i++) {
			if (reference[i].getParent() == null) {
				throw new RuntimeException("the referenced  entry is an orphan");
			}
			index[i] = getIndexInPool();
		}
		
		
	}
	
	
	

	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<reference.length; i++) {
			if (i>0) {
				buf.append(", ");
			}
			buf.append(reference[i].getPrintLabel());
		}
		
		return buf.toString();
	}

	protected abstract int getNumberOfReferences();
	
	
	

}
