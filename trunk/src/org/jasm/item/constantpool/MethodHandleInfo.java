package org.jasm.item.constantpool;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.parser.literals.SymbolReference;

public class MethodHandleInfo extends AbstractConstantPoolEntry implements INameReferencingEntry, IDescriptorReferencingEntry {
	
	private MethodHandleReferenceKind kind;
	private int index = -1;
	private SymbolReference refSymbolReference;
	private AbstractConstantPoolEntry reference;
	
	public MethodHandleInfo() {
		
	}
	
	public MethodHandleInfo(MethodHandleReferenceKind kind, AbstractConstantPoolEntry entry) {
		this.kind = kind;
		
	}

	@Override
	public short getTag() {
		return 15;
	}
	
	@Override
	public void readBody(IByteBuffer source, long offset) {
		this.kind = MethodHandleReferenceKind.getValue(source.readUnsignedByte(offset));
		this.index = source.readUnsignedShort(offset+1);
	}
	
	@Override
	public void writeBody(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset,(short) kind.getKind());
		target.writeUnsignedShort(offset+1, index);
		
	}

	@Override
	public int getLength() {
		return 4;
	}

	@Override
	protected void doResolve() {
		reference = (AbstractConstantPoolEntry)getParent().get(index-1);
	}
	
	@Override
	protected void doResolveAfterParse() {
		if (this.kind.getKind()>=1 && this.kind.getKind()<=4  ) {
			reference = getConstantPool().checkAndLoadFromSymbolTable(this, FieldrefInfo.class, refSymbolReference);
		}
		if (this.kind.getKind()>=5 && this.kind.getKind()<=8  ) {
			reference = getConstantPool().checkAndLoadFromSymbolTable(this, MethodrefInfo.class, refSymbolReference);
		}
		if (this.kind.getKind() == 9 ) {
			reference = getConstantPool().checkAndLoadFromSymbolTable(this, InterfaceMethodrefInfo.class, refSymbolReference);
		}
		if (reference != null) {
			updateIndex();
		}
	}

	
	public AbstractConstantPoolEntry getReference() {
		return reference;
	}

	public void setReference(AbstractConstantPoolEntry reference) {
		if (this.kind.getKind()>=1 && this.kind.getKind()<=4 && ! (reference instanceof FieldrefInfo) ) {
			throw new IllegalArgumentException("Illegal reference type for method handle kind "+kind+": "+reference.getClass().getName());
		}
		if (this.kind.getKind()>=5 && this.kind.getKind()<=8 && ! (reference instanceof MethodrefInfo) ) {
			throw new IllegalArgumentException("Illegal reference type for method handle kind "+kind+": "+reference.getClass().getName());
		}
		if (this.kind.getKind() == 9 && ! (reference instanceof InterfaceMethodrefInfo) ) {
			throw new IllegalArgumentException("Illegal reference type for method handle kind "+kind+": "+reference.getClass().getName());
		}
		this.reference = reference;
		updateIndex();
	}

	private void updateIndex() {
		
		if (reference.getParent() == null) {
			throw new RuntimeException("the referenced  entry is an orphan");
		}
		index = reference.getParent().indexOf(reference)+1;
	}



	public enum MethodHandleReferenceKind {
		GET_FIELD(1), GET_STATIC(2), PUT_FIELD(3), PUT_STATIC(4),
		INVOKE_VIRTUAL(5),INVOKE_STATIC(6),INVOKE_SPECIAL(7),NEW_INVOKE_SPECIAL(8),
		INVOKE_INTERFACE(9);

		MethodHandleReferenceKind(int kind) {
			this.kind = kind;
		}

		private final int kind;
		
		public int getKind() {
			return this.kind;
		}
		
		public static MethodHandleReferenceKind getValue(short value) {
			MethodHandleReferenceKind [] values = MethodHandleReferenceKind.values();
			for (MethodHandleReferenceKind v: values) {
				if (v.kind == value) {
					return v;
				}
			}
			
			throw new IllegalArgumentException("Illegal value for method reference kind: "+value);
		}
	}

	
	@Override
	public String getTypeLabel() {
		return "methodhandle";
	}
	
	

	@Override
	public String getPrintName() {
		return "const "+createReferenceKindLabel()+" methodhandle "+createConstName();
	}

	@Override
	public String getPrintArgs() {
		return reference.getSymbolName();
	}

	@Override
	public String getPrintComment() {
		return reference.getPrintComment();
	}

	@Override
	public String[] getReferencedDescriptors() {
		return ((AbstractRefInfo)reference).getReferencedDescriptors();
	}

	@Override
	public String[] getReferencedNames() {
		return ((AbstractRefInfo)reference).getReferencedNames();
	}
	
	private String createReferenceKindLabel() {
		switch (kind) {
			case GET_FIELD: return "getfield";
			case PUT_FIELD: return "putfield";
			case GET_STATIC: return "getstatic";
			case PUT_STATIC: return "putstatic";
			case INVOKE_INTERFACE: return "invokeinterface";
			case INVOKE_SPECIAL: return "invokespecial";
			case INVOKE_VIRTUAL: return "invokevirtual";
			case INVOKE_STATIC: return "invokestatic";
			case NEW_INVOKE_SPECIAL: return "newinvokespecial";
			default: throw new IllegalStateException(""+kind);
		}
	}

	public void setRefSymbolReference(SymbolReference refSymbolReference) {
		this.refSymbolReference = refSymbolReference;
	}

	public void setKind(MethodHandleReferenceKind kind) {
		this.kind = kind;
	}

	
	

	

}


