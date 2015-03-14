package org.jasm.item.constantpool;


import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.parser.literals.SymbolReference;


public abstract class AbstractReferenceEntry extends AbstractConstantPoolEntry implements IConstantPoolReference {

	private int[] index = null;
	protected SymbolReference[] referenceLabels = null;
	private AbstractConstantPoolEntry[] reference = null;
	
	private boolean referencesVerified = false;
	
	protected AbstractReferenceEntry() {
		
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
		for (int i=0;i<reference.length; i++) {
			target.writeUnsignedShort(offset+i*2, reference[i].getIndexInPool());
		}
	}

	

	@Override
	protected void doResolve() {
		reference = new AbstractConstantPoolEntry[getNumberOfReferences()];
		for (int i=0; i<index.length; i++) {
			reference[i] = (AbstractConstantPoolEntry)getParent().get(index[i]-1);
		}
	}
	
	
	
	@Override
	protected void doVerify() {
		
		
	}

	@Override
	protected void doResolveAfterParse() {
	
		reference = new AbstractConstantPoolEntry[getNumberOfReferences()];
		for (int i=0;i<referenceLabels.length; i++) {
			SymbolReference ref = referenceLabels[i];
			if (getConstantPool().getSymbolTable().contains(ref.getSymbolName())) {
				reference[i] = (AbstractConstantPoolEntry)getConstantPool().getSymbolTable().get(ref.getSymbolName());
			} else {
				emitError(ref, "unknown constant "+ref.getSymbolName());
			}
		}
	}
	
	
	public AbstractConstantPoolEntry[] getConstantReferences() {
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
			buf.append(reference[i].getSymbolName());
		}
		
		return buf.toString();
	}

	protected abstract int getNumberOfReferences();

	public SymbolReference[] getReferenceLabels() {
		return referenceLabels;
	}

	public void setReferenceLabels(SymbolReference[] referenceLabels) {
		this.referenceLabels = referenceLabels;
	}
	
	
	
	public void verifyReferences() {
		AbstractConstantPoolEntry[] expectedTypes = getExpectedReferenceTypes();
		if (expectedTypes.length != referenceLabels.length) {
			throw new IllegalStateException(expectedTypes.length+"!="+referenceLabels.length);
		}
		if (!referencesVerified) {
			for (int i=0;i<referenceLabels.length; i++) {
				
				if (!reference[i].getClass().equals(expectedTypes[i].getClass())) {
					emitError(referenceLabels[i], "wrong constant type, expected "+expectedTypes[i].getConstTypeLabel());
					
				} else {
					if (!reference[i].hasErrors()) {
						if (reference[i] instanceof AbstractReferenceEntry) {
							AbstractReferenceEntry ar = (AbstractReferenceEntry)reference[i];
							ar.verifyReferences();
							if (!ar.hasErrors()) {
								verifyReference(i, referenceLabels[i], reference[i]);
							} else {
								hasResolveErrors(true);
							}
						} else {
							verifyReference(i, referenceLabels[i], reference[i]);
						}
					} else {
						hasResolveErrors(true);
					}
				}
			}
			referencesVerified = true;
		}
	}
	
	protected abstract boolean verifyReference(int index, SymbolReference ref, AbstractConstantPoolEntry value);
	
	protected abstract AbstractConstantPoolEntry[] getExpectedReferenceTypes();
	

}
